package com.yasumu.feature.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yasumu.core.data.fake.FakeStockRepository
import com.yasumu.core.domain.model.Stock
import com.yasumu.core.domain.usecase.GetStockListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class StockListUiState(
    val isLoading: Boolean = true,
    val stocks: List<Stock> = emptyList(),
)

class StockListViewModel(
    private val getStockListUseCase: GetStockListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()

    init {
        observeStocks()
    }

    private fun observeStocks() {
        viewModelScope.launch {
            getStockListUseCase().collectLatest { stocks ->
                _uiState.value = StockListUiState(
                    isLoading = false,
                    stocks = stocks,
                )
            }
        }
    }
}

/**
 * 今は Hilt を使わず、Factory で Fake を差し込む。
 * 後で Room 実装に差し替えるときはここを書き換えればよい。
 */
class StockListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = FakeStockRepository()
        val useCase = GetStockListUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return StockListViewModel(useCase) as T
    }
}
