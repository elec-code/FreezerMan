package com.yasumu.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * カテゴリ設定用エンティティ
 *
 * Domain:
 *   Category(
 *     id: CategoryId,
 *     name: String,
 *   )
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
)
