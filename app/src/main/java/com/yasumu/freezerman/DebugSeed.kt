package com.yasumu.freezerman

import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.model.StockId
import com.yasumu.core.domain.repository.StockRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * DB が空ならサンプルデータを投入する。
 */
suspend fun seedStocksIfEmpty(repository: StockRepository) {
    val current = repository.getAllStocks().first()
    if (current.isNotEmpty()) return

    val nowInstant = Clock.System.now()
    val nowDateTime = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val today: LocalDate = nowDateTime.date

    val initialStocks = listOf(
        Stock(
            id = StockId(0),
            name = "鶏もも肉の照り焼き",
            quantity = 2,
            bestBeforeDate = today + DatePeriod(days = 30),
            cookedDate = today,
            registeredAt = nowInstant,
            categoryId = null,
            locationId = null,
        ),
        Stock(
            id = StockId(0),
            name = "ミートソース",
            quantity = 3,
            bestBeforeDate = today + DatePeriod(days = 60),
            cookedDate = today - DatePeriod(days = 1),
            registeredAt = nowInstant,
            categoryId = null,
            locationId = null,
        ),
        Stock(
            id = StockId(0),
            name = "冷凍ほうれん草",
            quantity = 1,
            bestBeforeDate = today + DatePeriod(days = 14),
            cookedDate = today,
            registeredAt = nowInstant,
            categoryId = null,
            locationId = null,
        ),
    )

    initialStocks.forEach { stock ->
        repository.upsertStock(stock)
    }
}
