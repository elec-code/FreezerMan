package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId

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

class UpsertCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {
    /**
     * - name の前後トリムや長さチェックなどのバリデーションもここに集約予定。
     * - 戻り値として保存後の Category を返す想定（新規時に採番された id を UI 側で使える）。
     */
    suspend operator fun invoke(command: UpsertCategoryCommand): Category {
        // 実装は 2-B 以降で作成
        TODO("implement in later step")
    }
}
