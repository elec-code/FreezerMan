package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: CategoryId): Category?

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(id: CategoryId)
}