package com.yasumu.feature.category_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.category.DeleteCategoryUseCase
import com.yasumu.core.domain.category.GetCategoriesUseCase
import com.yasumu.core.domain.category.ReorderCategoriesUseCase
import com.yasumu.core.domain.category.UpsertCategoryUseCase
import com.yasumu.core.domain.stock.CategoryId
import com.yasumu.core.domain.stock.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryEditViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val upsertCategoryUseCase: UpsertCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val reorderCategoriesUseCase: ReorderCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryEditUiState())
    val uiState: StateFlow<CategoryEditUiState> = _uiState

    private var hasStartedCollecting = false

    fun onAction(action: CategoryEditUiAction) {
        when (action) {
            is CategoryEditUiAction.OnAppear -> onAppear()
            is CategoryEditUiAction.OnAddClick -> onAddClick()
            is CategoryEditUiAction.OnEditClick -> onEditClick(action.categoryId)
            is CategoryEditUiAction.OnEditingNameChange -> onEditingNameChange(action.value)
            is CategoryEditUiAction.OnSheetConfirmClick -> onSheetConfirmClick()
            is CategoryEditUiAction.OnSheetCancelClick -> onSheetCancelClick()
            is CategoryEditUiAction.OnDeleteRequest -> onDeleteRequest()
            is CategoryEditUiAction.OnDeleteConfirm -> onDeleteConfirm()
            is CategoryEditUiAction.OnDeleteCancel -> onDeleteCancel()
            is CategoryEditUiAction.OnReorder -> onReorder(action.fromIndex, action.toIndex)
            is CategoryEditUiAction.OnReorderFinished -> onReorderFinished()
            is CategoryEditUiAction.OnMessageShown -> onMessageShown()
        }
    }

    private fun onAppear() {
        if (hasStartedCollecting) return
        hasStartedCollecting = true

        viewModelScope.launch {
            // 初回は isLoading = true のまま Flow を購読
            getCategoriesUseCase().collect { categories ->
                val items = mapToItemUiStateList(categories)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categories = items,
                    showEmptyMessage = items.isEmpty(),
                )
            }
        }
    }

    private fun mapToItemUiStateList(
        categories: List<com.yasumu.core.domain.category.Category>,
    ): List<CategoryItemUiState> =
        categories.map { category ->
            CategoryItemUiState(
                id = category.id,
                name = category.name,
                // isDragging は並び替え UI 実装時に更新するので、ここでは常に false
                isDragging = false,
            )
        }
    private fun onAddClick() {
        // 追加モードでボトムシートを開く（2-E で入力・確定ロジックを実装予定）
        _uiState.value = _uiState.value.copy(
            sheetState = CategoryEditSheetState.Add,
            editingCategoryId = null,
            editingCategoryName = "",
            isEditingNameError = false,
        )
    }

    private fun onSheetCancelClick() {
        // 入力は全部捨ててシートを閉じる
        _uiState.value = _uiState.value.copy(
            sheetState = CategoryEditSheetState.Hidden,
            editingCategoryId = null,
            editingCategoryName = "",
            isEditingNameError = false,
        )
    }

    // ここは 2-D 以降で本実装予定。2-C の間は「OK押しても何も起きない」でよい。
    private fun onSheetConfirmClick() {
        // 2-D で Upsert 処理を実装
    }

    private fun onEditClick(categoryId: CategoryId) { /* ... */ }
    private fun onEditingNameChange(value: String) { /* ... */ }
    private fun onDeleteRequest() { /* ... */ }
    private fun onDeleteConfirm() { /* ... */ }
    private fun onDeleteCancel() { /* ... */ }
    private fun onReorder(fromIndex: Int, toIndex: Int) { /* ... */ }
    private fun onReorderFinished() { /* ... */ }
    private fun onMessageShown() { /* ... */ }
}

class CategoryEditViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val stockRepository: StockRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)
        val upsertCategoryUseCase = UpsertCategoryUseCase(categoryRepository)
        val deleteCategoryUseCase = DeleteCategoryUseCase(categoryRepository, stockRepository)
        val reorderCategoriesUseCase = ReorderCategoriesUseCase(categoryRepository)

        return CategoryEditViewModel(
            getCategoriesUseCase = getCategoriesUseCase,
            upsertCategoryUseCase = upsertCategoryUseCase,
            deleteCategoryUseCase = deleteCategoryUseCase,
            reorderCategoriesUseCase = reorderCategoriesUseCase,
        ) as T
    }
}
