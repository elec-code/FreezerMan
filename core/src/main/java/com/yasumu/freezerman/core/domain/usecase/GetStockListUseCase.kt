package com.yasumu.freezerman.core.domain.usecase

import com.yasumu.freezerman.core.domain.model.Stock
import com.yasumu.freezerman.core.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow

/**
 * ストック一覧を取得するユースケース。
 * 将来、並び替えやフィルタ条件が増えてもここに寄せる。
 */
class GetStockListUseCase(
    private val stockRepository: StockRepository,
) {

    operator fun invoke(): Flow<List<Stock>> {
        // 並び順・フィルタなどのビジネスロジックはここに集約する
        return stockRepository.getAllStocks()
    }
}
