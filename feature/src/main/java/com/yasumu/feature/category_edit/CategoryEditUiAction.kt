package com.yasumu.feature.category_edit

import com.yasumu.core.domain.stock.CategoryId

sealed interface CategoryEditUiAction {
    // 画面初期表示
    data object OnAppear : CategoryEditUiAction

    // 追加開始（FAB タップ）
    data object OnAddClick : CategoryEditUiAction

    // 編集開始（ペンアイコンタップ）
    data class OnEditClick(val categoryId: CategoryId) : CategoryEditUiAction

    // ボトムシート内のカテゴリ名入力変更
    data class OnEditingNameChange(val value: String) : CategoryEditUiAction

    // ボトムシートの確定ボタン
    data object OnSheetConfirmClick : CategoryEditUiAction

    // ボトムシートのキャンセルボタン
    data object OnSheetCancelClick : CategoryEditUiAction

    // 削除要求（カテゴリ行 or シート内の削除ボタン）→ 確認ダイアログ表示
    data class OnDeleteRequest(val categoryId: CategoryId) : CategoryEditUiAction

    // 削除ダイアログで「削除」確定
    data object OnDeleteConfirm : CategoryEditUiAction

    // 削除ダイアログでキャンセル
    data object OnDeleteCancel : CategoryEditUiAction

    // 並び替え中（ドラッグ移動中のインデックス）
    data class OnReorder(
        val fromIndex: Int,
        val toIndex: Int,
    ) : CategoryEditUiAction

    // 並び替え完了（ドロップ後）
    data object OnReorderFinished : CategoryEditUiAction

    // Snackbar 等のメッセージが表示し終わったとき
    data object OnMessageShown : CategoryEditUiAction
}
