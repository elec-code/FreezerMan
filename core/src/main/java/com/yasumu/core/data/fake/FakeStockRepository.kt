package com.yasumu.core.data.fake

import com.yasumu.core.domain.stock.Stock
import com.yasumu.core.domain.stock.StockId
import com.yasumu.core.domain.stock.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class FakeStockRepository : StockRepository {

    private val stocksFlow = MutableStateFlow(initialStocks())

    /**
     * メモリ上で使う ID 採番用カウンタ。
     * - 初期データの最大 ID + 1 からスタートする。
     */
    private var nextId: Long =
        (stocksFlow.value.maxOfOrNull { it.id.value } ?: 0L) + 1L

    override fun getAllStocks(): Flow<List<Stock>> = stocksFlow.asStateFlow()

    override suspend fun getStockById(id: StockId): Stock? =
        stocksFlow.value.firstOrNull { it.id == id }

    override suspend fun upsertStock(stock: Stock): Stock {
        var result: Stock = stock

        stocksFlow.update { current ->
            if (stock.id.value == 0L) {
                // 新規: この場で ID を採番して追加
                val newStock = stock.copy(id = StockId(nextId++))
                result = newStock
                current + newStock
            } else {
                // 更新: 既存 ID を持つ要素を差し替え
                val index = current.indexOfFirst { it.id == stock.id }
                if (index == -1) {
                    // 万一見つからなければ「新規として追加」
                    current + stock
                } else {
                    current.toMutableList().also { it[index] = stock }
                }
            }
        }

        return result
    }

    override suspend fun deleteStock(id: StockId) {
        stocksFlow.update { current ->
            current.filterNot { it.id == id }
        }
    }

    private fun initialStocks(): List<Stock> {
        val now = Clock.System.now()
        val today: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

        return listOf(
            Stock(
                id = StockId(1),
                name = "冷凍からあげ",
                quantity = 2,
                bestBeforeDate = today + DatePeriod(days = 7),
                cookedDate = today,
                registeredAt = now,
                categoryId = null,
                locationId = null,
            ),
            Stock(
                id = StockId(2),
                name = "冷凍ハンバーグ",
                quantity = 3,
                bestBeforeDate = today + DatePeriod(days = 10),
                cookedDate = today,
                registeredAt = now,
                categoryId = null,
                locationId = null,
            ),
            Stock(
                id = StockId(3),
                name = "冷凍ほうれん草",
                quantity = 1,
                bestBeforeDate = today + DatePeriod(days = 14),
                cookedDate = today,
                registeredAt = now,
                categoryId = null,
                locationId = null,
            ),
        )
    }
}
