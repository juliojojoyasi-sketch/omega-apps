package com.example.omega.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String,
    val destinationTitle: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceMeters: Double = 0.0,
    val durationSeconds: Long = 0L,
    val type: String, // "SEARCH" or "ROUTE"
    val timestamp: Long = System.currentTimeMillis()
)
