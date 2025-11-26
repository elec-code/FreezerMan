package com.yasumu.core.domain.model

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
    val bestBeforeDate: kotlinx.datetime.LocalDate,
    val categoryId: CategoryId?,
    val locationId: LocationId?,
)
