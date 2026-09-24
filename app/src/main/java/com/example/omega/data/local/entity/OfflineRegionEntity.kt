package com.example.omega.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_regions")
data class OfflineRegionEntity(
    @PrimaryKey
    val id: String, // e.g., "mg_all", "mg_antananarivo", "mg_nosy_be"
    val name: String,
    val description: String,
    val minLat: Double,
    val maxLat: Double,
    val minLon: Double,
    val maxLon: Double,
    val minZoom: Int = 6,
    val maxZoom: Int = 14,
    val estimatedSizeMb: Int = 45,
    val downloadedTiles: Int = 0,
    val totalTiles: Int = 0,
    val isDownloaded: Boolean = false,
    val downloadedAt: Long = 0L
)
