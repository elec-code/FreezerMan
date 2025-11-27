package com.yasumu.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yasumu.core.data.local.dao.CategoryDao
import com.yasumu.core.data.local.dao.LocationDao
import com.yasumu.core.data.local.dao.StockDao
import com.yasumu.core.data.local.entity.CategoryEntity
import com.yasumu.core.data.local.entity.LocationEntity
import com.yasumu.core.data.local.entity.StockEntity

@Database(
    entities = [
        StockEntity::class,
        CategoryEntity::class,
        LocationEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    KotlinxDateTimeConverters::class,
)
abstract class FreezerDatabase : RoomDatabase() {

    abstract fun stockDao(): StockDao

    abstract fun categoryDao(): CategoryDao

    abstract fun locationDao(): LocationDao
}
