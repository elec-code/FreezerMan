package com.yasumu.freezerman

import android.content.Context
import androidx.room.Room
import com.yasumu.core.data.local.database.FreezerDatabase
import com.yasumu.core.data.local.repository.StockRepositoryImpl
import com.yasumu.core.domain.repository.StockRepository

class AppContainer(context: Context) {

    private val database: FreezerDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            FreezerDatabase::class.java,
            "freezer_database",
        ).build()
    }

    val stockRepository: StockRepository by lazy {
        StockRepositoryImpl(
            stockDao = database.stockDao(),
        )
    }
}
