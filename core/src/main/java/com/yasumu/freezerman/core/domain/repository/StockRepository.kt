package com.yasumu.freezerman.core.domain.repository

import com.yasumu.freezerman.core.domain.model.Stock
import com.yasumu.freezerman.core.domain.model.StockId
import kotlinx.coroutines.flow.Flow

/**
 * ストック（在庫）の取得・更新を担当するリポジトリIF。
 * 実装は Data 層（Room など）に置く。
 */
interface StockRepository {

    fun getAllStocks(): Flow<List<Stock>>

    suspend fun getStockById(id: StockId): Stock?

    suspend fun upsertStock(stock: Stock)

    suspend fun deleteStock(id: StockId)
}
