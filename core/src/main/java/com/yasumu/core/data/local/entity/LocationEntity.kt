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
 *   )
 *
 * ※ 並び順（ソート順）を Location 側に持たせるなら、
 *   将来的に sortOrder: Int などをここに追加して、
 *   Domain の Location にもフィールドを足す形が自然。
 */
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
)
