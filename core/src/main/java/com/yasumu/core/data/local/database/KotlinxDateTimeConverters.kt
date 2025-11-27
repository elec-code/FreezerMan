package com.yasumu.core.data.local.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Room で kotlinx.datetime を扱うための TypeConverter 群。
 *
 * - LocalDate  <-> Long(epochDay)
 * - Instant    <-> Long(epochMillis)
 *
 * ※ DB 上はどちらも INTEGER として保存される。
 */
object KotlinxDateTimeConverters {

    // ---------- LocalDate <-> Long(epochDay) ----------

    @TypeConverter
    @JvmStatic
    fun localDateToEpochDay(value: LocalDate?): Long? {
        return value?.toEpochDays()?.toLong()
    }

    @TypeConverter
    @JvmStatic
    fun epochDayToLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.fromEpochDays(it.toInt()) }
    }

    // ---------- Instant <-> Long(epochMillis) ----------

    @TypeConverter
    @JvmStatic
    fun instantToEpochMillis(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @TypeConverter
    @JvmStatic
    fun epochMillisToInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    // （必要になったら）
    // LocalDateTime を扱う場合のサンプル。
    // タイムゾーンは保存せず、常に UTC として保存する方針の例。
    @TypeConverter
    @JvmStatic
    fun localDateTimeToEpochMillis(value: LocalDateTime?): Long? {
        return value?.toInstant(TimeZone.UTC)?.toEpochMilliseconds()
    }

    @TypeConverter
    @JvmStatic
    fun epochMillisToLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC)
        }
    }
}
