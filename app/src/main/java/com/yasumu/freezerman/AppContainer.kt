package com.yasumu.freezerman

import android.content.Context
import androidx.room.Room
import com.yasumu.core.data.local.database.FreezerDatabase
import com.yasumu.core.data.local.repository.CategoryRepositoryImpl
import com.yasumu.core.data.local.repository.StockRepositoryImpl
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.stock.StockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
class AppContainer(context: Context) {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: FreezerDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FreezerDatabase::class.java,
            "freezer_database",
        ).build()

    val stockRepository: StockRepository =
        StockRepositoryImpl(database.stockDao())

    val categoryRepository: CategoryRepository =
        CategoryRepositoryImpl(database.categoryDao())

    /** 起動時に呼んで、必要ならサンプルデータを追加する */
    fun seedIfNeeded() {
        applicationScope.launch {
            seedStocksIfEmpty(stockRepository)
        }
    }}
