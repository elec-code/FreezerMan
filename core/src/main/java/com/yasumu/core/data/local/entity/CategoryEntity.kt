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
 *     order: CategoryOrder,
 *   )
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val sortOrder: Int,
)
