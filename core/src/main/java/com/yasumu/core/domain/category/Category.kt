package com.yasumu.core.domain.category

import com.yasumu.core.domain.model.CategoryId

@JvmInline
value class CategoryOrder(val value: Int)

/**
 * 食材カテゴリ（肉・魚・野菜など）
 */
data class Category(
    val id: CategoryId,
    val name: String,
    val order: CategoryOrder,
)
