package com.example.omega.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.omega.data.local.entity.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {
    @Query("SELECT * FROM markers ORDER BY createdAt DESC")
    fun getAllMarkers(): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM markers WHERE id = :id LIMIT 1")
    suspend fun getMarkerById(id: Long): MarkerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(marker: MarkerEntity): Long

    @Update
    suspend fun updateMarker(marker: MarkerEntity)

    @Delete
    suspend fun deleteMarker(marker: MarkerEntity)

    @Query("DELETE FROM markers WHERE id = :id")
    suspend fun deleteMarkerById(id: Long)
}
