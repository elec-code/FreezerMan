package com.yasumu.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 保管場所設定用エンティティ
 *
 * Domain:
 *   Location(
 *     id: LocationId,
 *     name: String,
 *     order: LocationOrder,
 *   )
 */
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val sortOrder: Int,
)
