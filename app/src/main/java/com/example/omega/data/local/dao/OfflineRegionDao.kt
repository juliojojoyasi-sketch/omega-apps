package com.example.omega.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.omega.data.local.entity.OfflineRegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineRegionDao {
    @Query("SELECT * FROM offline_regions")
    fun getAllRegions(): Flow<List<OfflineRegionEntity>>

    @Query("SELECT * FROM offline_regions WHERE id = :id LIMIT 1")
    suspend fun getRegionById(id: String): OfflineRegionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRegions(regions: List<OfflineRegionEntity>)

    @Update
    suspend fun updateRegion(region: OfflineRegionEntity)

    @Query("UPDATE offline_regions SET isDownloaded = :isDownloaded, downloadedTiles = :downloaded, totalTiles = :total, downloadedAt = :timestamp WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, isDownloaded: Boolean, downloaded: Int, total: Int, timestamp: Long)
}
