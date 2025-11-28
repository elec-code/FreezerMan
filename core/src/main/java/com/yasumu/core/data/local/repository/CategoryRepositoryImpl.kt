package com.yasumu.core.data.local.repository

import com.yasumu.core.data.local.dao.CategoryDao
import com.yasumu.core.data.local.entity.CategoryEntity
import com.yasumu.core.domain.category.Category
import com.yasumu.core.domain.stock.CategoryId
import com.yasumu.core.domain.category.CategoryOrder
import com.yasumu.core.domain.category.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room をバックエンドとした CategoryRepository の実装。
 *
 * - category.id.value == 0L の場合は新規 INSERT として扱い、
 *   DB の autoGenerate で採番された ID を使う。
 * - それ以外の場合は UPDATE として扱う。
 */
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getCategoryById(id: CategoryId): Category? {
        return categoryDao.getCategoryById(id.value)
            ?.toDomain()
    }

    override suspend fun upsertCategory(category: Category) {
        if (category.id.value == 0L) {
            // 新規追加: ID=0 で insert し、Room に ID 採番を任せる
            val entityForInsert = category.toEntity().copy(id = 0L)
            categoryDao.insert(entityForInsert)
        } else {
            // 更新: 既存 ID を持つレコードを上書き
            categoryDao.update(category.toEntity())
        }
    }

    override suspend fun deleteCategory(id: CategoryId) {
        categoryDao.deleteById(id.value)
    }
}

/*
 * ===== Mapper =====
 * Domain <-> Entity の相互変換
 */

// Entity -> Domain
private fun CategoryEntity.toDomain(): Category {
    return Category(
        id = CategoryId(id),
        name = name,
        order = CategoryOrder(sortOrder),
    )
}

// Domain -> Entity
private fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id.value,
        name = name,
        sortOrder = order.value,
    )
}
