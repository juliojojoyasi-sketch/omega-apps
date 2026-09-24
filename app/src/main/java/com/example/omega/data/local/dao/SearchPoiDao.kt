package com.example.omega.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.omega.data.local.entity.SearchPoiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchPoiDao {
    @Query("SELECT * FROM search_pois ORDER BY popularityScore DESC")
    fun getAllPois(): Flow<List<SearchPoiEntity>>

    @Query("""
        SELECT * FROM search_pois 
        WHERE name LIKE '%' || :query || '%' 
           OR localName LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN name LIKE :query || '%' THEN 1 
                WHEN name LIKE '%' || :query || '%' THEN 2 
                ELSE 3 
            END,
            popularityScore DESC
        LIMIT 30
    """)
    suspend fun searchPois(query: String): List<SearchPoiEntity>

    @Query("""
        SELECT * FROM search_pois 
        WHERE (:category IS NULL OR category = :category)
          AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        ORDER BY popularityScore DESC
        LIMIT 30
    """)
    suspend fun searchPoisWithCategory(query: String, category: String?): List<SearchPoiEntity>

    @Query("SELECT COUNT(*) FROM search_pois")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(pois: List<SearchPoiEntity>)
}
