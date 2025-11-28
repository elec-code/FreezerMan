package com.yasumu.core.domain.stock

class DeleteStockUseCase(
    private val stockRepository: StockRepository,
) {

    suspend operator fun invoke(id: StockId) {
        stockRepository.deleteStock(id)
    }
}