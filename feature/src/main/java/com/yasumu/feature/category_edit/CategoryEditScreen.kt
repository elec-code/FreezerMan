package com.yasumu.feature.category_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.stock.StockRepository

@Composable
fun CategoryEditRoute(
    categoryRepository: CategoryRepository,
    stockRepository: StockRepository,
    viewModel: CategoryEditViewModel = viewModel(
        factory = CategoryEditViewModelFactory(categoryRepository, stockRepository),
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    // 初回表示時にカテゴリ一覧の購読を開始
    LaunchedEffect(Unit) {
        viewModel.onAction(CategoryEditUiAction.OnAppear)
    }

    CategoryEditScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(
    uiState: CategoryEditUiState,
    onAction: (CategoryEditUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showSheet = uiState.sheetState != CategoryEditSheetState.Hidden

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("カテゴリ編集") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(CategoryEditUiAction.OnAddClick) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "カテゴリを追加",
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.showEmptyMessage -> {
                    Text(
                        text = "カテゴリがありません",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(uiState.categories) { item ->
                            CategoryRow(item = item)
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        DummyCategoryEditSheet(
            sheetState = uiState.sheetState,
            onAction = onAction,
        )
    }
}

@Composable
private fun CategoryRow(
    item: CategoryItemUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(text = item.name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DummyCategoryEditSheet(
    sheetState: CategoryEditSheetState,
    onAction: (CategoryEditUiAction) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onAction(CategoryEditUiAction.OnSheetCancelClick)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            val title = when (sheetState) {
                CategoryEditSheetState.Add -> "カテゴリを追加"
                CategoryEditSheetState.Edit -> "カテゴリを編集"
                CategoryEditSheetState.Hidden -> ""
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "ここに入力 UI が入ります（2-E で実装）")

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { onAction(CategoryEditUiAction.OnSheetCancelClick) },
                ) {
                    Text("キャンセル")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = { onAction(CategoryEditUiAction.OnSheetConfirmClick) },
                ) {
                    Text("OK")
                }
            }
        }
    }
}
