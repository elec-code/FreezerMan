package com.yasumu.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room 用の在庫エンティティ
 *
 * Domain:
 *   Stock(
 *     id: StockId,
 *     name: String,
 *     quantity: Int,
 *     bestBeforeDate: LocalDate,
 *     cookedDate: LocalDate,
 *     registeredAt: Instant,
 *     categoryId: CategoryId?,
 *     locationId: LocationId?,
 *   )
 *
 * Date/Time 型は TypeConverter で変換する前提で Long に落として保持する。
 */
@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val quantity: Int,

    // LocalDate(bestBeforeDate) -> Long(epochDay)
    val bestBeforeDateEpochDay: Long,

    // LocalDate(cookedDate) -> Long(epochDay)
    val cookedDateEpochDay: Long,

    // Instant(registeredAt) -> Long(epochMillis)
    val registeredAtEpochMillis: Long,

    // CategoryId? / LocationId? の実体
    val categoryId: Long?,
    val locationId: Long?,
)
