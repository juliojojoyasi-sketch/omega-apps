package com.example.omega.ui.map

import android.content.Context
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.omega.data.local.entity.MarkerEntity
import com.example.omega.gps.GpsData
import com.example.omega.map.MapLayerType
import com.example.omega.map.OmegaTileSources
import com.example.omega.routing.RouteResult
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

@Composable
fun OmegaMapView(
    selectedLayer: MapLayerType,
    gpsData: GpsData,
    compassAzimuth: Float,
    isCompassFollowing: Boolean,
    markers: List<MarkerEntity>,
    activeRoute: RouteResult?,
    mapTarget: Triple<Double, Double, Double>?,
    onTargetHandled: () -> Unit,
    onMapLongClick: (lat: Double, lon: Double) -> Unit,
    onMarkerClick: (MarkerEntity) -> Unit,
    onMapReady: (MapView) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            isTilesScaledToDpi = true
            minZoomLevel = 4.0
            maxZoomLevel = 19.0

            // Enable two-finger rotation gesture
            val rotationOverlay = RotationGestureOverlay(this).apply {
                isEnabled = true
            }
            overlays.add(rotationOverlay)

            // Center initially on Antananarivo, Madagascar
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(-18.9102, 47.5255))
        }
    }

    // Lifecycle handling for mapview
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    // Pass mapview reference to caller (for tile caching, offline downloads)
    LaunchedEffect(mapView) {
        onMapReady(mapView)
    }

    // Update tile source when user switches layer
    LaunchedEffect(selectedLayer) {
        val source = OmegaTileSources.getSourceForType(selectedLayer)
        mapView.setTileSource(source)
    }

    // Handle map camera target movement (recenter or select place)
    LaunchedEffect(mapTarget) {
        mapTarget?.let { (lat, lon, zoom) ->
            mapView.controller.animateTo(GeoPoint(lat, lon), zoom, 800L)
            onTargetHandled()
        }
    }

    // Update map rotation when compass follow is active
    LaunchedEffect(compassAzimuth, isCompassFollowing) {
        if (isCompassFollowing) {
            mapView.mapOrientation = -compassAzimuth
        }
    }

    // Update overlays: User GPS, Markers, Long Click Listener, Route Polyline
    LaunchedEffect(gpsData, markers, activeRoute) {
        // Keep rotation overlay (index 0)
        val rotationOverlay = mapView.overlays.firstOrNull { it is RotationGestureOverlay }
        mapView.overlays.clear()
        if (rotationOverlay != null) {
            mapView.overlays.add(rotationOverlay)
        }

        // 1. Long-press map events overlay to place markers
        val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    onMapLongClick(p.latitude, p.longitude)
                    return true
                }
                return false
            }
        })
        mapView.overlays.add(eventsOverlay)

        // 2. Active Route Polyline Overlay
        activeRoute?.let { route ->
            if (route.points.isNotEmpty()) {
                // Route casing (Google Maps navy blue border)
                val casingPolyline = Polyline().apply {
                    outlinePaint.color = AndroidColor.parseColor("#1558B0")
                    outlinePaint.strokeWidth = 18f
                    outlinePaint.strokeCap = Paint.Cap.ROUND
                    outlinePaint.strokeJoin = Paint.Join.ROUND
                    setPoints(route.points.map { GeoPoint(it.latitude, it.longitude) })
                }
                mapView.overlays.add(casingPolyline)

                // Route main Google Maps Blue (#1A73E8) line
                val mainPolyline = Polyline().apply {
                    outlinePaint.color = AndroidColor.parseColor("#1A73E8")
                    outlinePaint.strokeWidth = 12f
                    outlinePaint.strokeCap = Paint.Cap.ROUND
                    outlinePaint.strokeJoin = Paint.Join.ROUND
                    setPoints(route.points.map { GeoPoint(it.latitude, it.longitude) })
                }
                mapView.overlays.add(mainPolyline)

                // Destination marker
                val lastPoint = route.points.last()
                val destMarker = Marker(mapView).apply {
                    position = GeoPoint(lastPoint.latitude, lastPoint.longitude)
                    title = route.destinationName
                    snippet = "Destination"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                mapView.overlays.add(destMarker)
            }
        }

        // 3. User Saved Markers
        markers.forEach { m ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(m.latitude, m.longitude)
                title = m.title
                snippet = m.note.ifEmpty { m.category }
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                setOnMarkerClickListener { _, _ ->
                    onMarkerClick(m)
                    true
                }
            }
            mapView.overlays.add(marker)
        }

        // 4. User GPS Position Marker & Accuracy Ring (Google Maps Royal Blue dot)
        val userPoint = GeoPoint(gpsData.latitude, gpsData.longitude)

        // Google Maps Accuracy Light Blue Circle
        val accuracyMeters = gpsData.accuracyMeters.coerceIn(5f, 150f).toDouble()
        val circlePolygon = Polygon.pointsAsCircle(userPoint, accuracyMeters)
        val accuracyOverlay = Polygon().apply {
            points = circlePolygon
            fillPaint.color = AndroidColor.parseColor("#221A73E8")
            outlinePaint.color = AndroidColor.parseColor("#661A73E8")
            outlinePaint.strokeWidth = 2.0f
        }
        mapView.overlays.add(accuracyOverlay)

        // User Position Pin (Google Blue Dot)
        val userMarker = Marker(mapView).apply {
            position = userPoint
            title = "Votre position"
            val alt = gpsData.altitude?.let { "${it.toInt()} m" } ?: "--"
            snippet = "Vitesse: ${gpsData.speedKmh.toInt()} km/h • Altitude: $alt"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            rotation = gpsData.bearing
        }
        mapView.overlays.add(userMarker)

        mapView.invalidate()
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
