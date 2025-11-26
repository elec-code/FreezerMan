package com.yasumu.freezerman.core.domain.repository

import com.yasumu.freezerman.core.domain.model.Category
import com.yasumu.freezerman.core.domain.model.CategoryId
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: CategoryId): Category?

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(id: CategoryId)
}
