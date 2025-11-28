package com.yasumu.core.domain.category

import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) {
    /**
     * 並び順は Category.order の昇順を基本とする想定。
     */
    operator fun invoke(): Flow<List<Category>> =
        categoryRepository.getAllCategories()
}
