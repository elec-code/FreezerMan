package com.yasumu.feature.category_edit

import com.yasumu.core.domain.stock.CategoryId


data class CategoryEditUiState(
    val isLoading: Boolean = true,

    // 画面に表示するカテゴリ一覧（UI 用に必要なら別モデルに変換）
    val categories: List<CategoryItemUiState> = emptyList(),

    // 「カテゴリがありません」メッセージ表示用
    val showEmptyMessage: Boolean = false,

    // ボトムシートの状態
    val sheetState: CategoryEditSheetState = CategoryEditSheetState.Hidden,

    // シート内の入力値（追加・編集共通で流用）
    val editingCategoryName: String = "",
    val isEditingNameError: Boolean = false,

    // 編集対象カテゴリ（追加時は null）
    val editingCategoryId: CategoryId? = null,

    // 削除確認ダイアログの表示状態
    val deleteConfirmDialog: DeleteConfirmDialogState = DeleteConfirmDialogState.Hidden,

    // Snackbar等で表示するメッセージ（表示後に消すワンショット用）
    val userMessage: UiMessage? = null,
)

data class CategoryItemUiState(
    val id: CategoryId,
    val name: String,
    val isDragging: Boolean = false,
)

/**
 * ボトムシートのモード
 */
sealed interface CategoryEditSheetState {
    data object Hidden : CategoryEditSheetState
    data object Add : CategoryEditSheetState
    data object Edit : CategoryEditSheetState
}

/**
 * 削除確認ダイアログの状態
 */
sealed interface DeleteConfirmDialogState {
    data object Hidden : DeleteConfirmDialogState

    /**
     * count: このカテゴリを使用しているストック数
     */
    data class Visible(
        val categoryId: CategoryId,
        val count: Int,
    ) : DeleteConfirmDialogState
}

/**
 * Snackbar などの一時メッセージ用
 * id は consume 済みを識別するためのもの
 */
data class UiMessage(
    val id: Long,
    val message: String,
)
