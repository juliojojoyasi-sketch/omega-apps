package com.example.omega.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.omega.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 50")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE type = 'SEARCH' ORDER BY timestamp DESC LIMIT 20")
    fun getSearchHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE type = 'ROUTE' ORDER BY timestamp DESC LIMIT 20")
    fun getRouteHistory(): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity): Long

    @Query("DELETE FROM history")
    suspend fun clearAllHistory()

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)
}
