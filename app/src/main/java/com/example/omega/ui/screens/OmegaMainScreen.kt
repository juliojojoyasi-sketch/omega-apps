package com.example.omega.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omega.data.local.entity.MarkerEntity
import com.example.omega.gps.GpsData
import com.example.omega.ui.OmegaViewModel
import com.example.omega.ui.components.OmegaCompassHud
import com.example.omega.ui.components.OmegaFavoritesSheet
import com.example.omega.ui.components.OmegaLayerSelector
import com.example.omega.ui.components.OmegaMarkerDialog
import com.example.omega.ui.components.OmegaNavigationBanner
import com.example.omega.ui.components.OmegaOfflineRegionsDialog
import com.example.omega.ui.components.OmegaSearchDialog
import com.example.omega.ui.components.OmegaTopBar
import com.example.omega.ui.map.OmegaMapView
import com.example.ui.theme.OmegaAmberGold
import com.example.ui.theme.OmegaCrimson
import com.example.ui.theme.OmegaDarkBackground
import com.example.ui.theme.OmegaDarkSurface
import com.example.ui.theme.OmegaDarkSurfaceBorder
import com.example.ui.theme.OmegaDarkSurfaceElevated
import com.example.ui.theme.OmegaEmeraldGreen
import com.example.ui.theme.OmegaNeonCyan
import com.example.ui.theme.OmegaTextPrimary
import com.example.ui.theme.OmegaTextSecondary
import org.osmdroid.views.MapView
import java.util.Locale

@Composable
fun OmegaMainScreen(
    viewModel: OmegaViewModel = viewModel()
) {
    val context = LocalContext.current

    // Request Location Permissions gracefully on launch
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.locationTracker.startTracking()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // State collections
    val gpsData by viewModel.locationTracker.gpsState.collectAsState()
    val compassAzimuth by viewModel.compassSensor.azimuth.collectAsState()
    val isCompassFollowing by viewModel.isCompassFollowing.collectAsState()
    val isEcoMode by viewModel.isEcoMode.collectAsState()
    val selectedLayer by viewModel.selectedLayer.collectAsState()

    val markers by viewModel.markers.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val history by viewModel.history.collectAsState()
    val regions by viewModel.regions.collectAsState()

    val activeRoute by viewModel.activeRoute.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val showSearchDialog by viewModel.showSearchDialog.collectAsState()
    val showMarkerDialog by viewModel.showMarkerDialog.collectAsState()
    val pendingMarkerLocation by viewModel.pendingMarkerLocation.collectAsState()
    val showFavoritesSheet by viewModel.showFavoritesSheet.collectAsState()
    val showRegionsDialog by viewModel.showRegionsDialog.collectAsState()

    val downloadingRegionId by viewModel.downloadingRegionId.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val cacheSizeBytes by viewModel.cacheSizeBytes.collectAsState()

    val mapTarget by viewModel.mapTarget.collectAsState()

    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var clickedMarker by remember { mutableStateOf<MarkerEntity?>(null) }
    var showLandingPageSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = OmegaDarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Interactive OSM Map Layer
            OmegaMapView(
                selectedLayer = selectedLayer,
                gpsData = gpsData,
                compassAzimuth = compassAzimuth,
                isCompassFollowing = isCompassFollowing,
                markers = markers,
                activeRoute = activeRoute,
                mapTarget = mapTarget,
                onTargetHandled = { viewModel.clearMapTarget() },
                onMapLongClick = { lat, lon ->
                    viewModel.openAddMarker(lat, lon)
                },
                onMarkerClick = { marker ->
                    clickedMarker = marker
                },
                onMapReady = { mv ->
                    mapViewRef = mv
                },
                modifier = Modifier.fillMaxSize()
            )

            // 2. Top Controls Column (Top Bar, Layer Switcher, Active Navigation Banner)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                OmegaTopBar(
                    gpsData = gpsData,
                    compassAzimuth = compassAzimuth,
                    isEcoMode = isEcoMode,
                    onSearchClick = { viewModel.openSearch() },
                    onEcoToggle = { viewModel.toggleEcoMode() },
                    onFavoritesClick = { viewModel.openFavoritesSheet() },
                    onOfflineRegionsClick = { viewModel.openRegionsDialog() },
                    onLandingPageClick = { showLandingPageSheet = true }
                )

                activeRoute?.let { route ->
                    OmegaNavigationBanner(
                        route = route,
                        currentStepIndex = currentStepIndex,
                        onStopNavigation = { viewModel.stopNavigation() },
                        onNextStep = { viewModel.nextNavigationStep() }
                    )
                } ?: run {
                    // Floating Layer Selector (Carte, Satellite, Relief)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        OmegaLayerSelector(
                            selectedLayer = selectedLayer,
                            onLayerSelected = { viewModel.setMapLayer(it) }
                        )
                    }
                }
            }

            // 3. Bottom HUD Overlay: Telemetry, Speedometer, Compass Rose, Map Controls
            OmegaCompassHud(
                gpsData = gpsData,
                compassAzimuth = compassAzimuth,
                isCompassFollowing = isCompassFollowing,
                onRecenterClick = { viewModel.recenterGps() },
                onCompassClick = { viewModel.toggleCompassFollowing() },
                onZoomInClick = { mapViewRef?.controller?.zoomIn() },
                onZoomOutClick = { mapViewRef?.controller?.zoomOut() },
                onAddMarkerClick = { viewModel.openAddMarker() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )

            // 4. Modals & Dialogs
            if (showSearchDialog) {
                OmegaSearchDialog(
                    gpsData = gpsData,
                    searchQuery = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.onSearchCategoryChange(it) },
                    searchResults = searchResults,
                    searchHistory = history,
                    isSearching = isSearching,
                    onSelectPoi = { poi, navigate ->
                        viewModel.closeSearch()
                        if (navigate) {
                            viewModel.startNavigationTo(poi.latitude, poi.longitude, poi.name)
                        } else {
                            viewModel.centerOn(poi.latitude, poi.longitude, 15.0)
                        }
                    },
                    onAddFavorite = { poi ->
                        viewModel.addFavorite(poi.name, poi.region, poi.latitude, poi.longitude)
                    },
                    onDismiss = { viewModel.closeSearch() }
                )
            }

            if (showMarkerDialog) {
                val loc = pendingMarkerLocation ?: com.example.omega.routing.LatLon(gpsData.latitude, gpsData.longitude)
                OmegaMarkerDialog(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    onSaveMarker = { title, note, cat, hex ->
                        viewModel.saveMarker(title, note, cat, hex)
                    },
                    onDismiss = { viewModel.closeMarkerDialog() }
                )
            }

            if (showFavoritesSheet) {
                OmegaFavoritesSheet(
                    markers = markers,
                    favorites = favorites,
                    history = history,
                    onSelectLocation = { lat, lon, name, navigate ->
                        viewModel.closeFavoritesSheet()
                        if (navigate) {
                            viewModel.startNavigationTo(lat, lon, name)
                        } else {
                            viewModel.centerOn(lat, lon, 15.0)
                        }
                    },
                    onDeleteMarker = { viewModel.deleteMarker(it) },
                    onDeleteFavorite = { viewModel.deleteFavorite(it) },
                    onClearHistory = { viewModel.clearHistory() },
                    onDismiss = { viewModel.closeFavoritesSheet() }
                )
            }

            if (showRegionsDialog) {
                OmegaOfflineRegionsDialog(
                    regions = regions,
                    downloadingRegionId = downloadingRegionId,
                    downloadProgress = downloadProgress,
                    cacheSizeBytes = cacheSizeBytes,
                    onDownloadRegion = { region ->
                        mapViewRef?.let { mv ->
                            viewModel.downloadRegion(region, mv)
                        }
                    },
                    onDismiss = { viewModel.closeRegionsDialog() }
                )
            }

            if (showLandingPageSheet) {
                OmegaLandingPageSheet(
                    onDismiss = { showLandingPageSheet = false }
                )
            }

            // Clicked Marker info sheet dialog
            clickedMarker?.let { m ->
                AlertDialog(
                    onDismissRequest = { clickedMarker = null },
                    containerColor = OmegaDarkSurface,
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = OmegaNeonCyan
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = m.title,
                                color = OmegaTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    },
                    text = {
                        Column {
                            if (m.note.isNotEmpty()) {
                                Text(
                                    text = m.note,
                                    color = OmegaTextSecondary,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Text(
                                text = "Catégorie : ${m.category}",
                                color = OmegaAmberGold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = String.format(Locale.US, "Position : %.5f, %.5f", m.latitude, m.longitude),
                                color = OmegaTextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.startNavigationTo(m.latitude, m.longitude, m.title)
                                clickedMarker = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OmegaNeonCyan,
                                contentColor = OmegaDarkBackground
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Itinéraire", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        Row {
                            IconButton(
                                onClick = {
                                    viewModel.deleteMarker(m)
                                    clickedMarker = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Supprimer",
                                    tint = OmegaCrimson
                                )
                            }
                            OutlinedButton(onClick = { clickedMarker = null }) {
                                Text("Fermer", color = OmegaTextSecondary)
                            }
                        }
                    }
                )
            }
        }
    }
}
