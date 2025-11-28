package com.yasumu.core.domain.stock

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@JvmInline
value class StockId(val value: Long)

@JvmInline
value class CategoryId(val value: Long)

@JvmInline
value class LocationId(val value: Long)

data class Stock(
    val id: StockId,
    val name: String,
    val quantity: Int,

    val bestBeforeDate: LocalDate,
    val cookedDate: LocalDate,

    val registeredAt: Instant,

    val categoryId: CategoryId?,
    val locationId: LocationId?,
)
