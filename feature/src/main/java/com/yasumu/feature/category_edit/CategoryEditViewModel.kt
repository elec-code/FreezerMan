package com.yasumu.feature.category_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.category.DeleteCategoryUseCase
import com.yasumu.core.domain.category.GetCategoriesUseCase
import com.yasumu.core.domain.category.ReorderCategoriesUseCase
import com.yasumu.core.domain.category.UpsertCategoryCommand
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
        // 追加モードでボトムシートを開く
        _uiState.value = _uiState.value.copy(
            sheetState = CategoryEditSheetState.Add,
            editingCategoryId = null,
            editingCategoryName = "",
            isEditingNameError = false,
        )
    }

    private fun onEditClick(categoryId: CategoryId) {
        // 編集対象を現在の一覧から探し、ボトムシートを Edit モードで開く
        val target = _uiState.value.categories.find { it.id == categoryId } ?: return

        _uiState.value = _uiState.value.copy(
            sheetState = CategoryEditSheetState.Edit,
            editingCategoryId = categoryId,
            editingCategoryName = target.name,
            isEditingNameError = false,
        )
    }

    private fun onEditingNameChange(value: String) {
        // 入力中のカテゴリ名を保持。エラーフラグは一旦リセット。
        _uiState.value = _uiState.value.copy(
            editingCategoryName = value,
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

    private fun onSheetConfirmClick() {
        val current = _uiState.value
        val rawName = current.editingCategoryName

        viewModelScope.launch {
            val trimmed = rawName.trim()

            // 空文字はこの段階で弾いて、UI にエラー状態を通知
            if (trimmed.isEmpty()) {
                _uiState.value = current.copy(isEditingNameError = true)
                return@launch
            }

            try {
                val command = UpsertCategoryCommand(
                    id = current.editingCategoryId,
                    name = trimmed,
                )

                upsertCategoryUseCase(command)

                // 成功時: シートを閉じる
                // 一覧の再読み込みは Flow(getCategoriesUseCase) に任せる
                _uiState.value = _uiState.value.copy(
                    sheetState = CategoryEditSheetState.Hidden,
                    editingCategoryId = null,
                    editingCategoryName = "",
                    isEditingNameError = false,
                )
            } catch (e: IllegalArgumentException) {
                // バリデーションエラーなど → ひとまず名前入力エラーとして扱う
                _uiState.value = _uiState.value.copy(
                    isEditingNameError = true,
                )
            } catch (e: Exception) {
                // 想定外エラーは Snackbar 用メッセージに載せる
                _uiState.value = _uiState.value.copy(
                    userMessage = UiMessage(
                        id = System.currentTimeMillis(),
                        message = "カテゴリの保存に失敗しました",
                    ),
                )
            }
        }
    }

    private fun onMessageShown() {
        _uiState.value = _uiState.value.copy(
            userMessage = null,
        )
    }

    /**
     * 削除要求（ダイアログを出すための事前確認）
     *
     * - editingCategoryId が null の場合は何もしない（Edit モード以外の誤タップ防止）
     * - DeleteCategoryUseCase から使用件数を取得し、ダイアログ状態を Visible にする
     */
    private fun onDeleteRequest() {
        val targetId = _uiState.value.editingCategoryId ?: return

        viewModelScope.launch {
            try {
                val count = deleteCategoryUseCase.getUsageCount(targetId)
                _uiState.value = _uiState.value.copy(
                    deleteConfirmDialog = DeleteConfirmDialogState.Visible(
                        categoryId = targetId,
                        count = count,
                    ),
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    userMessage = UiMessage(
                        id = System.currentTimeMillis(),
                        message = "カテゴリの使用状況の取得に失敗しました",
                    ),
                )
            }
        }
    }

    /**
     * 削除ダイアログで「削除」確定
     *
     * - DeleteConfirmDialogState.Visible から categoryId を取得
     * - DeleteCategoryUseCase.delete を実行
     * - 成功時にダイアログとシートを閉じて Snackbar を表示
     * - 一覧の更新は getCategoriesUseCase の Flow に任せる
     */
    private fun onDeleteConfirm() {
        val dialogState = _uiState.value.deleteConfirmDialog
        if (dialogState !is DeleteConfirmDialogState.Visible) return

        viewModelScope.launch {
            try {
                deleteCategoryUseCase.delete(dialogState.categoryId)

                _uiState.value = _uiState.value.copy(
                    deleteConfirmDialog = DeleteConfirmDialogState.Hidden,
                    sheetState = CategoryEditSheetState.Hidden,
                    editingCategoryId = null,
                    editingCategoryName = "",
                    isEditingNameError = false,
                    userMessage = UiMessage(
                        id = System.currentTimeMillis(),
                        message = "カテゴリを削除しました",
                    ),
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    deleteConfirmDialog = DeleteConfirmDialogState.Hidden,
                    userMessage = UiMessage(
                        id = System.currentTimeMillis(),
                        message = "カテゴリの削除に失敗しました",
                    ),
                )
            }
        }
    }

    /**
     * 削除ダイアログでキャンセル
     *
     * - deleteConfirmDialog を Hidden に戻すだけ
     */
    private fun onDeleteCancel() {
        _uiState.value = _uiState.value.copy(
            deleteConfirmDialog = DeleteConfirmDialogState.Hidden,
        )
    }

    private fun onReorder(fromIndex: Int, toIndex: Int) { /* 2-H で実装 */ }

    private fun onReorderFinished() { /* 2-H で実装 */ }
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
