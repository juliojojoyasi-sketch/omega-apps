package com.example.omega.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GpsData(
    val latitude: Double = -18.9102, // Default Antananarivo center
    val longitude: Double = 47.5255,
    val altitude: Double? = 1275.0,
    val speedKmh: Float = 0f,
    val bearing: Float = 0f,
    val accuracyMeters: Float = 5f,
    val isFixAcquired: Boolean = false,
    val provider: String = "GPS",
    val timestamp: Long = System.currentTimeMillis()
)

class LocationTracker(private val context: Context) : LocationListener {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    private val _gpsState = MutableStateFlow(GpsData())
    val gpsState: StateFlow<GpsData> = _gpsState.asStateFlow()

    private var isTracking = false
    private var isEcoMode = false

    @SuppressLint("MissingPermission")
    fun startTracking(ecoMode: Boolean = false) {
        if (isTracking || locationManager == null) return
        isEcoMode = ecoMode

        val minTimeMs = if (ecoMode) 4000L else 1000L
        val minDistanceM = if (ecoMode) 3f else 1f

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTimeMs,
                    minDistanceM,
                    this
                )
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTimeMs,
                    minDistanceM,
                    this
                )
            }

            // Get last known location for instant display
            val lastGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val lastNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val best = lastGps ?: lastNet
            if (best != null) {
                onLocationChanged(best)
            }
            isTracking = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun updateEcoMode(ecoMode: Boolean) {
        if (isEcoMode != ecoMode && isTracking) {
            stopTracking()
            startTracking(ecoMode)
        }
    }

    fun stopTracking() {
        if (!isTracking) return
        try {
            locationManager?.removeUpdates(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isTracking = false
    }

    override fun onLocationChanged(location: Location) {
        val speedKmh = if (location.hasSpeed()) (location.speed * 3.6f) else 0f
        val altitude = if (location.hasAltitude()) location.altitude else null

        _gpsState.value = GpsData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = altitude,
            speedKmh = speedKmh,
            bearing = if (location.hasBearing()) location.bearing else _gpsState.value.bearing,
            accuracyMeters = if (location.hasAccuracy()) location.accuracy else 10f,
            isFixAcquired = true,
            provider = location.provider ?: "GPS",
            timestamp = location.time
        )
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}
