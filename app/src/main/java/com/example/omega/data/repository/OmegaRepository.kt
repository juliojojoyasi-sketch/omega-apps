package com.example.omega.data.repository

import com.example.omega.data.local.AppDatabase
import com.example.omega.data.local.entity.FavoriteEntity
import com.example.omega.data.local.entity.HistoryEntity
import com.example.omega.data.local.entity.MarkerEntity
import com.example.omega.data.local.entity.OfflineRegionEntity
import com.example.omega.data.local.entity.SearchPoiEntity
import kotlinx.coroutines.flow.Flow

class OmegaRepository(private val database: AppDatabase) {
    private val markerDao = database.markerDao()
    private val favoriteDao = database.favoriteDao()
    private val historyDao = database.historyDao()
    private val regionDao = database.offlineRegionDao()
    private val searchPoiDao = database.searchPoiDao()

    // Markers
    val allMarkers: Flow<List<MarkerEntity>> = markerDao.getAllMarkers()
    suspend fun addMarker(marker: MarkerEntity): Long = markerDao.insertMarker(marker)
    suspend fun deleteMarker(marker: MarkerEntity) = markerDao.deleteMarker(marker)
    suspend fun deleteMarkerById(id: Long) = markerDao.deleteMarkerById(id)

    // Favorites
    val allFavorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
    suspend fun addFavorite(favorite: FavoriteEntity): Long = favoriteDao.insertFavorite(favorite)
    suspend fun deleteFavorite(favorite: FavoriteEntity) = favoriteDao.deleteFavorite(favorite)
    suspend fun deleteFavoriteById(id: Long) = favoriteDao.deleteFavoriteById(id)
    suspend fun isFavorite(lat: Double, lon: Double): Boolean = favoriteDao.isFavorite(lat, lon)

    // History
    val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory()
    val searchHistory: Flow<List<HistoryEntity>> = historyDao.getSearchHistory()
    val routeHistory: Flow<List<HistoryEntity>> = historyDao.getRouteHistory()
    suspend fun recordHistory(item: HistoryEntity) = historyDao.insertHistory(item)
    suspend fun clearHistory() = historyDao.clearAllHistory()
    suspend fun deleteHistoryById(id: Long) = historyDao.deleteHistoryById(id)

    // Regions
    val allRegions: Flow<List<OfflineRegionEntity>> = regionDao.getAllRegions()
    suspend fun insertInitialRegions(regions: List<OfflineRegionEntity>) = regionDao.insertRegions(regions)
    suspend fun updateRegionStatus(id: String, isDownloaded: Boolean, downloaded: Int, total: Int, timestamp: Long) =
        regionDao.updateDownloadStatus(id, isDownloaded, downloaded, total, timestamp)

    // POIs / Offline search
    val allPois: Flow<List<SearchPoiEntity>> = searchPoiDao.getAllPois()
    suspend fun searchPois(query: String, category: String? = null): List<SearchPoiEntity> {
        return if (category == null || category == "TOUT") {
            searchPoiDao.searchPois(query.trim())
        } else {
            searchPoiDao.searchPoisWithCategory(query.trim(), category)
        }
    }
    suspend fun getPoiCount(): Int = searchPoiDao.getCount()
    suspend fun seedPois(pois: List<SearchPoiEntity>) = searchPoiDao.insertAll(pois)
}
