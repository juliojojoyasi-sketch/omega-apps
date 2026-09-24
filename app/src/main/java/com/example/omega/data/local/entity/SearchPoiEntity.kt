package com.example.omega.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_pois",
    indices = [
        Index(value = ["name"]),
        Index(value = ["category"])
    ]
)
data class SearchPoiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val localName: String = "",
    val category: String, // "VILLE", "RUE", "VILLAGE", "LIEU", "TOURISME", "TRANSPORT", "NATURE"
    val latitude: Double,
    val longitude: Double,
    val region: String = "Madagascar",
    val description: String = "",
    val popularityScore: Int = 100
)
