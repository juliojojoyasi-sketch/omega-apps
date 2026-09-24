package com.example.omega.map

import android.content.Context
import com.example.omega.data.local.entity.OfflineRegionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView
import java.io.File

class OfflineTileManager(private val context: Context) {

    init {
        // Configure OSMDroid storage path inside internal application cache
        val osmdroidBasePath = File(context.filesDir, "osmdroid")
        val osmdroidTilePath = File(osmdroidBasePath, "tiles")
        if (!osmdroidBasePath.exists()) osmdroidBasePath.mkdirs()
        if (!osmdroidTilePath.exists()) osmdroidTilePath.mkdirs()

        // Clean any blocked legacy Mapnik or watermarked CartoVoyager cache tiles
        for (oldDir in listOf("Mapnik", "CartoVoyager")) {
            val oldCache = File(osmdroidTilePath, oldDir)
            if (oldCache.exists()) {
                oldCache.deleteRecursively()
            }
        }

        Configuration.getInstance().osmdroidBasePath = osmdroidBasePath
        Configuration.getInstance().osmdroidTileCache = osmdroidTilePath
        Configuration.getInstance().userAgentValue = "OmegaMapsApp/1.0 (${context.packageName}; Android)"
        Configuration.getInstance().cacheMapTileCount = 300.toShort()
        Configuration.getInstance().cacheMapTileOvershoot = 60.toShort()
    }

    fun applyEcoMode(isEcoEnabled: Boolean) {
        if (isEcoEnabled) {
            Configuration.getInstance().cacheMapTileCount = 150.toShort()
            Configuration.getInstance().cacheMapTileOvershoot = 30.toShort()
            Configuration.getInstance().tileDownloadThreads = 2.toShort()
        } else {
            Configuration.getInstance().cacheMapTileCount = 350.toShort()
            Configuration.getInstance().cacheMapTileOvershoot = 80.toShort()
            Configuration.getInstance().tileDownloadThreads = 4.toShort()
        }
    }

    suspend fun downloadRegion(
        mapView: MapView,
        region: OfflineRegionEntity,
        onProgress: (downloaded: Int, total: Int) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val cacheManager = CacheManager(mapView)
            val boundingBox = BoundingBox(
                region.maxLat,
                region.maxLon,
                region.minLat,
                region.minLon
            )

            val minZoom = region.minZoom
            val maxZoom = region.maxZoom.coerceAtMost(14) // Balanced resolution for mobile storage

            val totalTiles = cacheManager.possibleTilesInArea(boundingBox, minZoom, maxZoom)

            cacheManager.downloadAreaAsync(
                context,
                boundingBox,
                minZoom,
                maxZoom,
                object : CacheManager.CacheManagerCallback {
                    override fun onTaskComplete() {
                        onProgress(totalTiles, totalTiles)
                    }

                    override fun updateProgress(progress: Int, current: Int, total: Int, errors: Int) {
                        onProgress(current, total)
                    }

                    override fun downloadStarted() {}
                    override fun onTaskFailed(errors: Int) {}
                    override fun setPossibleTilesInArea(total: Int) {}
                }
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getCacheSizeBytes(): Long {
        val tileDir = Configuration.getInstance().osmdroidTileCache ?: return 0L
        return getFolderSize(tileDir)
    }

    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        if (file.isDirectory) {
            file.listFiles()?.forEach { child ->
                size += getFolderSize(child)
            }
        } else {
            size += file.length()
        }
        return size
    }
}
