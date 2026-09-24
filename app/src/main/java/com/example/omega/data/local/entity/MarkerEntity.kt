package com.example.omega.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markers")
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val note: String = "",
    val latitude: Double,
    val longitude: Double,
    val category: String = "Général",
    val colorHex: String = "#00E5FF",
    val createdAt: Long = System.currentTimeMillis()
)
