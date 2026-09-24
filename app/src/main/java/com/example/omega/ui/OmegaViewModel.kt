package com.example.omega.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega.data.local.AppDatabase
import com.example.omega.data.local.entity.FavoriteEntity
import com.example.omega.data.local.entity.HistoryEntity
import com.example.omega.data.local.entity.MarkerEntity
import com.example.omega.data.local.entity.OfflineRegionEntity
import com.example.omega.data.local.entity.SearchPoiEntity
import com.example.omega.data.repository.OmegaRepository
import com.example.omega.gps.CompassSensor
import com.example.omega.gps.GpsData
import com.example.omega.gps.LocationTracker
import com.example.omega.map.DefaultMadagascarData
import com.example.omega.map.MapLayerType
import com.example.omega.map.OfflineTileManager
import com.example.omega.routing.LatLon
import com.example.omega.routing.OfflineRoutingEngine
import com.example.omega.routing.RouteResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView

class OmegaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = OmegaRepository(database)
    val tileManager = OfflineTileManager(application)
    val routingEngine = OfflineRoutingEngine()

    val locationTracker = LocationTracker(application)
    val compassSensor = CompassSensor(application)

    // Data Flows from Room
    val markers: StateFlow<List<MarkerEntity>> = repository.allMarkers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<HistoryEntity>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val regions: StateFlow<List<OfflineRegionEntity>> = repository.allRegions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI States
    private val _selectedLayer = MutableStateFlow(MapLayerType.CARTE)
    val selectedLayer: StateFlow<MapLayerType> = _selectedLayer.asStateFlow()

    private val _isEcoMode = MutableStateFlow(false)
    val isEcoMode: StateFlow<Boolean> = _isEcoMode.asStateFlow()

    private val _isCompassFollowing = MutableStateFlow(false)
    val isCompassFollowing: StateFlow<Boolean> = _isCompassFollowing.asStateFlow()

    // Navigation & Routing State
    private val _activeRoute = MutableStateFlow<RouteResult?>(null)
    val activeRoute: StateFlow<RouteResult?> = _activeRoute.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("TOUT")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchPoiEntity>>(emptyList())
    val searchResults: StateFlow<List<SearchPoiEntity>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Dialog & UI Sheet Flags
    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog: StateFlow<Boolean> = _showSearchDialog.asStateFlow()

    private val _showMarkerDialog = MutableStateFlow(false)
    val showMarkerDialog: StateFlow<Boolean> = _showMarkerDialog.asStateFlow()

    private val _pendingMarkerLocation = MutableStateFlow<LatLon?>(null)
    val pendingMarkerLocation: StateFlow<LatLon?> = _pendingMarkerLocation.asStateFlow()

    private val _showFavoritesSheet = MutableStateFlow(false)
    val showFavoritesSheet: StateFlow<Boolean> = _showFavoritesSheet.asStateFlow()

    private val _showRegionsDialog = MutableStateFlow(false)
    val showRegionsDialog: StateFlow<Boolean> = _showRegionsDialog.asStateFlow()

    // Region download state
    private val _downloadingRegionId = MutableStateFlow<String?>(null)
    val downloadingRegionId: StateFlow<String?> = _downloadingRegionId.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    private val _cacheSizeBytes = MutableStateFlow(0L)
    val cacheSizeBytes: StateFlow<Long> = _cacheSizeBytes.asStateFlow()

    // Map viewport movement target event (lat, lon, zoom)
    private val _mapTarget = MutableStateFlow<Triple<Double, Double, Double>?>(null)
    val mapTarget: StateFlow<Triple<Double, Double, Double>?> = _mapTarget.asStateFlow()

    init {
        // Start sensors & GPS
        locationTracker.startTracking()
        compassSensor.start()

        // Seed initial Madagascar database if empty
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.getPoiCount()
            if (count == 0) {
                repository.seedPois(DefaultMadagascarData.INITIAL_POIS)
                repository.insertInitialRegions(DefaultMadagascarData.INITIAL_REGIONS)
            }
            updateCacheSize()
            // Pre-load default popular search items
            _searchResults.value = repository.searchPois("")
        }
    }

    fun updateCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            _cacheSizeBytes.value = tileManager.getCacheSizeBytes()
        }
    }

    fun setMapLayer(layer: MapLayerType) {
        _selectedLayer.value = layer
    }

    fun toggleEcoMode() {
        val newMode = !_isEcoMode.value
        _isEcoMode.value = newMode
        locationTracker.updateEcoMode(newMode)
        tileManager.applyEcoMode(newMode)

        // In eco mode, if compass is not actively following, pause compass sensor
        if (newMode && !_isCompassFollowing.value) {
            compassSensor.stop()
        } else {
            compassSensor.start()
        }
    }

    fun toggleCompassFollowing() {
        val newFollowing = !_isCompassFollowing.value
        _isCompassFollowing.value = newFollowing
        if (newFollowing) {
            compassSensor.start()
        }
    }

    fun recenterGps() {
        val gps = locationTracker.gpsState.value
        _mapTarget.value = Triple(gps.latitude, gps.longitude, 16.0)
    }

    fun centerOn(latitude: Double, longitude: Double, zoom: Double = 15.0) {
        _mapTarget.value = Triple(latitude, longitude, zoom)
    }

    fun clearMapTarget() {
        _mapTarget.value = null
    }

    // Navigation methods
    fun startNavigationTo(destLat: Double, destLon: Double, destName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val gps = locationTracker.gpsState.value
            val route = routingEngine.calculateRoute(
                startLat = gps.latitude,
                startLon = gps.longitude,
                destLat = destLat,
                destLon = destLon,
                destinationName = destName
            )

            _activeRoute.value = route
            _currentStepIndex.value = 0

            if (route != null) {
                // Record in history
                repository.recordHistory(
                    HistoryEntity(
                        query = destName,
                        destinationTitle = destName,
                        latitude = destLat,
                        longitude = destLon,
                        distanceMeters = route.totalDistanceMeters,
                        durationSeconds = route.totalDurationSeconds,
                        type = "ROUTE"
                    )
                )
                // Center map along first step
                centerOn(gps.latitude, gps.longitude, 16.0)
            }
        }
    }

    fun stopNavigation() {
        _activeRoute.value = null
        _currentStepIndex.value = 0
    }

    fun nextNavigationStep() {
        val route = _activeRoute.value ?: return
        if (_currentStepIndex.value < route.steps.size - 1) {
            _currentStepIndex.value += 1
            val nextStep = route.steps[_currentStepIndex.value]
            centerOn(nextStep.startLat, nextStep.startLon, 16.5)
        }
    }

    // Search methods
    fun openSearch() {
        _showSearchDialog.value = true
        onSearchQueryChange(_searchQuery.value)
    }

    fun closeSearch() {
        _showSearchDialog.value = false
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        viewModelScope.launch(Dispatchers.IO) {
            _isSearching.value = true
            val results = repository.searchPois(query, _selectedCategory.value)
            _searchResults.value = results
            _isSearching.value = false

            if (query.isNotBlank() && query.length >= 3) {
                repository.recordHistory(
                    HistoryEntity(
                        query = query.trim(),
                        type = "SEARCH"
                    )
                )
            }
        }
    }

    fun onSearchCategoryChange(category: String) {
        _selectedCategory.value = category
        onSearchQueryChange(_searchQuery.value)
    }

    // Marker Dialog & actions
    fun openAddMarker(lat: Double? = null, lon: Double? = null) {
        val target = if (lat != null && lon != null) {
            LatLon(lat, lon)
        } else {
            val gps = locationTracker.gpsState.value
            LatLon(gps.latitude, gps.longitude)
        }
        _pendingMarkerLocation.value = target
        _showMarkerDialog.value = true
    }

    fun closeMarkerDialog() {
        _showMarkerDialog.value = false
        _pendingMarkerLocation.value = null
    }

    fun saveMarker(title: String, note: String, category: String, colorHex: String) {
        val loc = _pendingMarkerLocation.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMarker(
                MarkerEntity(
                    title = title,
                    note = note,
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    category = category,
                    colorHex = colorHex
                )
            )
            closeMarkerDialog()
        }
    }

    fun deleteMarker(marker: MarkerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMarker(marker)
        }
    }

    // Favorites
    fun openFavoritesSheet() {
        _showFavoritesSheet.value = true
    }

    fun closeFavoritesSheet() {
        _showFavoritesSheet.value = false
    }

    fun addFavorite(title: String, subtitle: String, lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorite(
                FavoriteEntity(
                    title = title,
                    subtitle = subtitle,
                    latitude = lat,
                    longitude = lon
                )
            )
        }
    }

    fun deleteFavorite(fav: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(fav)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistory()
        }
    }

    // Offline regions
    fun openRegionsDialog() {
        _showRegionsDialog.value = true
        updateCacheSize()
    }

    fun closeRegionsDialog() {
        _showRegionsDialog.value = false
    }

    fun downloadRegion(region: OfflineRegionEntity, mapView: MapView) {
        if (_downloadingRegionId.value != null) return
        _downloadingRegionId.value = region.id
        _downloadProgress.value = 0.05f

        viewModelScope.launch(Dispatchers.IO) {
            val success = tileManager.downloadRegion(mapView, region) { current, total ->
                val p = if (total > 0) current.toFloat() / total.toFloat() else 0f
                _downloadProgress.value = p.coerceIn(0.05f, 1f)
            }

            if (success) {
                repository.updateRegionStatus(
                    id = region.id,
                    isDownloaded = true,
                    downloaded = region.totalTiles.coerceAtLeast(100),
                    total = region.totalTiles.coerceAtLeast(100),
                    timestamp = System.currentTimeMillis()
                )
            }

            _downloadingRegionId.value = null
            _downloadProgress.value = 1f
            updateCacheSize()
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationTracker.stopTracking()
        compassSensor.stop()
    }
}
