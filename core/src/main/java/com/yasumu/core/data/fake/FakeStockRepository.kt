package com.yasumu.core.data.fake

import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.model.StockId
import com.yasumu.core.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class FakeStockRepository : StockRepository {

    private val stocksFlow = MutableStateFlow(initialStocks())

    override fun getAllStocks(): Flow<List<Stock>> = stocksFlow.asStateFlow()

    override suspend fun getStockById(id: StockId): Stock? =
        stocksFlow.value.firstOrNull { it.id == id }

    override suspend fun upsertStock(stock: Stock) {
        stocksFlow.update { current ->
            val index = current.indexOfFirst { it.id == stock.id }
            if (index == -1) {
                current + stock
            } else {
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
        val today = LocalDate.now()
        return listOf(
            Stock(
                id = StockId(1),
                name = "冷凍からあげ",
                quantity = 2,
                bestBeforeDate = today.plusDays(7),
                categoryId = null,
                locationId = null,
            ),
            Stock(
                id = StockId(2),
                name = "ごはんパック",
                quantity = 5,
                bestBeforeDate = today.plusDays(30),
                categoryId = null,
                locationId = null,
            ),
            Stock(
                id = StockId(3),
                name = "冷凍ほうれん草",
                quantity = 1,
                bestBeforeDate = today.plusDays(14),
                categoryId = null,
                locationId = null,
            ),
        )
    }
}
