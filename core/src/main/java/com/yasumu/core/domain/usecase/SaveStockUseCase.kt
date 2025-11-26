package com.yasumu.freezerman.core.domain.usecase

import com.yasumu.freezerman.core.domain.model.Stock
import com.yasumu.freezerman.core.domain.repository.StockRepository

/**
 * ストックの新規作成・更新を行うユースケース。
 */
class SaveStockUseCase(
    private val stockRepository: StockRepository,
) {

    suspend operator fun invoke(stock: Stock) {
        // バリデーションや補正が必要ならここに書く
        // 例: quantity <= 0 の場合は例外、など
        stockRepository.upsertStock(stock)
    }
}
