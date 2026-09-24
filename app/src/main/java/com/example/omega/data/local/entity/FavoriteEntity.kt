package com.example.omega.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val latitude: Double,
    val longitude: Double,
    val category: String = "Favori",
    val addedAt: Long = System.currentTimeMillis()
)
