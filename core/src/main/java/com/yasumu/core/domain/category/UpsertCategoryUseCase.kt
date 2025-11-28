package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId
import kotlinx.coroutines.flow.first

/**
 * カテゴリ追加/更新の入力データ
 *
 * - id == null: 新規追加
 * - id != null: 既存カテゴリの更新
 */
data class UpsertCategoryCommand(
    val id: CategoryId?,
    val name: String,
)

/**
 * カテゴリの追加 / 更新を行う UseCase。
 *
 * - 新規追加時: 現在の最大 order の次の値を採番して末尾に追加する。
 * - 更新時    : 既存レコードの order は維持したまま name を更新する。
 * - name の空文字チェックなど、単純なバリデーションもここで行う。
 */
class UpsertCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(command: UpsertCategoryCommand) {
        val trimmedName = command.name.trim()
        require(trimmedName.isNotEmpty()) {
            "カテゴリ名は空にできません。"
        }

        if (command.id == null) {
            // ===== 新規追加 =====
            val currentList = categoryRepository.getAllCategories().first()
            val nextOrderValue = (currentList.maxOfOrNull { it.order.value } ?: 0) + 1

            val newCategory = Category(
                id = CategoryId(0L), // Repository 側で INSERT 時に採番
                name = trimmedName,
                order = CategoryOrder(nextOrderValue),
            )

            categoryRepository.upsertCategory(newCategory)
        } else {
            // ===== 更新 =====
            val existing = categoryRepository.getCategoryById(command.id)
                ?: throw IllegalArgumentException("Category not found. id=${command.id.value}")

            val updated = existing.copy(name = trimmedName)
            categoryRepository.upsertCategory(updated)
        }
    }
}
