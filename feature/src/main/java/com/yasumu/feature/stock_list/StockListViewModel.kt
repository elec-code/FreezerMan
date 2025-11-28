package com.yasumu.feature.stock_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yasumu.core.domain.stock.Stock
import com.yasumu.core.domain.stock.StockRepository
import com.yasumu.core.domain.stock.GetStockListUseCase
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

/*
* Repository注入用Factory
 */
class StockListViewModelFactory(
    private val stockRepository: StockRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val useCase = GetStockListUseCase(stockRepository)
        @Suppress("UNCHECKED_CAST")
        return StockListViewModel(useCase) as T
    }
}
