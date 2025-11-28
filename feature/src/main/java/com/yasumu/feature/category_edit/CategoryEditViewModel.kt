package com.yasumu.feature.category_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasumu.core.domain.category.DeleteCategoryUseCase
import com.yasumu.core.domain.category.GetCategoriesUseCase
import com.yasumu.core.domain.category.ReorderCategoriesUseCase
import com.yasumu.core.domain.category.UpsertCategoryUseCase
import com.yasumu.core.domain.stock.CategoryId
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
        viewModelScope.launch {
            // GetCategoriesUseCase の collect で uiState.categories を更新
            // 実装は 2-B 以降
        }
    }

    private fun onAddClick() { /* ... */ }
    private fun onEditClick(categoryId: CategoryId) { /* ... */ }
    private fun onEditingNameChange(value: String) { /* ... */ }
    private fun onSheetConfirmClick() { /* ... */ }
    private fun onSheetCancelClick() { /* ... */ }
    private fun onDeleteRequest() { /* ... */ }
    private fun onDeleteConfirm() { /* ... */ }
    private fun onDeleteCancel() { /* ... */ }
    private fun onReorder(fromIndex: Int, toIndex: Int) { /* ... */ }
    private fun onReorderFinished() { /* ... */ }
    private fun onMessageShown() { /* ... */ }
}
