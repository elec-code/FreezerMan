package com.yasumu.core.domain.category

import com.yasumu.core.domain.stock.CategoryId
import com.yasumu.core.domain.stock.StockRepository
import kotlinx.coroutines.flow.first

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val stockRepository: StockRepository,
) {

    /**
     * 指定カテゴリを参照している Stock の件数を返す。
     * 確認ダイアログの表示可否やメッセージ文言に利用。
     */
    suspend fun getUsageCount(categoryId: CategoryId): Int {
        val allStocks = stockRepository.getAllStocks().first()
        return allStocks.count { it.categoryId == categoryId }
    }

    /**
     * - 指定カテゴリを参照している Stock の categoryId を null に更新
     * - その後に Category を削除
     */
    suspend fun delete(categoryId: CategoryId) {
        // 1. このカテゴリを使っている Stock の categoryId を null にする
        val allStocks = stockRepository.getAllStocks().first()
        val targetStocks = allStocks.filter { it.categoryId == categoryId }

        for (stock in targetStocks) {
            val cleared = stock.copy(categoryId = null)
            stockRepository.upsertStock(cleared)
        }

        // 2. カテゴリ本体を削除
        categoryRepository.deleteCategory(categoryId)
    }
}
