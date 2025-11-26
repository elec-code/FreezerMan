package com.yasumu.freezerman.core.domain.model

import java.time.LocalDate

@JvmInline
value class StockId(val value: Long)

@JvmInline
value class CategoryId(val value: Long)

@JvmInline
value class LocationId(val value: Long)

/**
 * FreezerManで扱う在庫アイテムのDomainモデル
 */
data class Stock(
    val id: StockId,
    val name: String,
    val quantity: Int,
    val bestBeforeDate: LocalDate,
    val categoryId: CategoryId?,
    val locationId: LocationId?,
)
