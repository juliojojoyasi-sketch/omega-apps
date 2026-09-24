package com.example.omega.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.omega.data.local.dao.FavoriteDao
import com.example.omega.data.local.dao.HistoryDao
import com.example.omega.data.local.dao.MarkerDao
import com.example.omega.data.local.dao.OfflineRegionDao
import com.example.omega.data.local.dao.SearchPoiDao
import com.example.omega.data.local.entity.FavoriteEntity
import com.example.omega.data.local.entity.HistoryEntity
import com.example.omega.data.local.entity.MarkerEntity
import com.example.omega.data.local.entity.OfflineRegionEntity
import com.example.omega.data.local.entity.SearchPoiEntity

@Database(
    entities = [
        MarkerEntity::class,
        FavoriteEntity::class,
        HistoryEntity::class,
        OfflineRegionEntity::class,
        SearchPoiEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun markerDao(): MarkerDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
    abstract fun offlineRegionDao(): OfflineRegionDao
    abstract fun searchPoiDao(): SearchPoiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "omega_maps_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
