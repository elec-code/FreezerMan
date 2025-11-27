package com.yasumu.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

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
 * Date/Time 型は TypeConverter で Long に変換され、DB には INTEGER として保存される。
 */
@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val quantity: Int,

    // LocalDate -> INTEGER (epochDay)
    val bestBeforeDate: LocalDate,
    val cookedDate: LocalDate,

    // Instant -> INTEGER (epochMillis)
    val registeredAt: Instant,

    // CategoryId? / LocationId? の実体
    val categoryId: Long?,
    val locationId: Long?,
)
