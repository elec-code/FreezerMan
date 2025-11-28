package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId
import com.yasumu.core.domain.stock.StockRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val stockRepository: StockRepository,
) {

    /**
     * 指定カテゴリを参照している Stock の件数を返す。
     * 確認ダイアログの表示可否やメッセージ文言に利用。
     */
    suspend fun getUsageCount(categoryId: CategoryId): Int {
        // 実装は後続ステップで
        TODO("implement in later step")
    }

    /**
     * - 指定カテゴリを参照している Stock の categoryId を null に更新
     * - その後に Category を削除
     */
    suspend fun delete(categoryId: CategoryId) {
        // 実装は後続ステップで
        TODO("implement in later step")
    }
}
