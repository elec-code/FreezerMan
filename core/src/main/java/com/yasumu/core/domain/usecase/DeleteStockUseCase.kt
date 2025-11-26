package com.yasumu.core.domain.usecase

import com.yasumu.core.domain.model.StockId
import com.yasumu.core.domain.repository.StockRepository

class DeleteStockUseCase(
    private val stockRepository: StockRepository,
) {

    suspend operator fun invoke(id: StockId) {
        stockRepository.deleteStock(id)
    }
}
