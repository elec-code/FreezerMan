package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId
import kotlinx.coroutines.flow.first

class ReorderCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) {
    /**
     * @param orderedCategoryIds 画面上での新しい並び順（先頭 = 最小 order）
     */
    suspend operator fun invoke(orderedCategoryIds: List<CategoryId>) {
        if (orderedCategoryIds.isEmpty()) return

        // 現在のカテゴリ一覧を取得（永続化されている最新状態）
        val current = categoryRepository.getAllCategories().first()
        if (current.isEmpty()) return

        val currentById = current.associateBy { it.id }

        // orderedCategoryIds に含まれていないカテゴリはそのままの order を維持する。
        // また、現状存在しない ID が含まれている場合は何もせず終了する（安全側）。
        if (orderedCategoryIds.any { it !in currentById }) {
            return
        }

        // 画面上の順序に合わせて order を 0,1,2,... に振り直す
        val updated = orderedCategoryIds.mapIndexedNotNull { index, id ->
            val category = currentById.getValue(id)
            val newOrder = CategoryOrder(index)
            if (category.order == newOrder) {
                null // 変更不要
            } else {
                category.copy(order = newOrder)
            }
        }

        // 変更があるカテゴリのみ upsert
        for (category in updated) {
            categoryRepository.upsertCategory(category)
        }
    }
}
