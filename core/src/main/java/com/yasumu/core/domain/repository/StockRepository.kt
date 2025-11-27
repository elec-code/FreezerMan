package com.yasumu.core.domain.repository

import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.model.StockId
import kotlinx.coroutines.flow.Flow

/**
 * ストック（在庫）の取得・更新を担当するリポジトリIF。
 * 実装は Data 層（Room など）に置く。
 */
interface StockRepository {

    fun getAllStocks(): Flow<List<Stock>>

    suspend fun getStockById(id: StockId): Stock?

    /**
     * 在庫を保存する。
     *
     * - stock.id が StockId(0) の場合: 新規追加として扱い、保存後に採番された ID を持つ Stock を返す。
     * - それ以外の場合: 既存レコードを更新し、更新後の Stock を返す。
     */
    suspend fun upsertStock(stock: Stock): Stock

    suspend fun deleteStock(id: StockId)
}
