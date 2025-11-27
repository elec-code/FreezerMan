package com.yasumu.core.data.fake

import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.model.StockId
import com.yasumu.core.domain.repository.StockRepository
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

    override fun getAllStocks(): Flow<List<Stock>> = stocksFlow.asStateFlow()

    override suspend fun getStockById(id: StockId): Stock? =
        stocksFlow.value.firstOrNull { it.id == id }

    override suspend fun upsertStock(stock: Stock) {
        stocksFlow.update { current ->
            val index = current.indexOfFirst { it.id == stock.id }
            if (index == -1) {
                // 新規追加：呼び出し側で registeredAt を確定して渡している前提
                current + stock
            } else {
                // 更新：registeredAt は呼び出し側で維持している前提（Fake では触らない）
                current.toMutableList().also { it[index] = stock }
            }
        }
    }

    override suspend fun deleteStock(id: StockId) {
        stocksFlow.update { current ->
            current.filterNot { it.id == id }
        }
    }

    private fun initialStocks(): List<Stock> {
        val now = Clock.System.now()
        val today: LocalDate =
            now.toLocalDateTime(TimeZone.currentSystemDefault())
                .date

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
                name = "ごはんパック",
                quantity = 5,
                bestBeforeDate = today + DatePeriod(days = 30),
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
