package com.yasumu.freezerman.core.domain.usecase

import com.yasumu.freezerman.core.domain.model.StockId
import com.yasumu.freezerman.core.domain.repository.StockRepository

class DeleteStockUseCase(
    private val stockRepository: StockRepository,
) {

    suspend operator fun invoke(id: StockId) {
        stockRepository.deleteStock(id)
    }
}
