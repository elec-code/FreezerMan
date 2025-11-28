package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId

class ReorderCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) {
    /**
     * @param orderedCategoryIds 画面上での新しい並び順（先頭 = 最小 order）
     */
    suspend operator fun invoke(orderedCategoryIds: List<CategoryId>) {
        // 実装は後続ステップで
        TODO("implement in later step")
    }
}
