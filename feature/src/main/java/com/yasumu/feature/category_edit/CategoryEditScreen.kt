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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    val snackbarHostState = remember { SnackbarHostState() }

    // userMessage が来たら Snackbar を表示して消費
    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message.message)
        onAction(CategoryEditUiAction.OnMessageShown)
    }

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                            CategoryRow(
                                item = item,
                                onEditClick = { id ->
                                    onAction(CategoryEditUiAction.OnEditClick(id))
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        CategoryEditSheet(
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
private fun CategoryRow(
    item: CategoryItemUiState,
    onEditClick: (com.yasumu.core.domain.stock.CategoryId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(item.name) },
            trailingContent = {
                IconButton(onClick = { onEditClick(item.id) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "カテゴリ名を編集",
                    )
                }
            },
        )
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEditSheet(
    uiState: CategoryEditUiState,
    onAction: (CategoryEditUiAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val isEditMode = uiState.sheetState is CategoryEditSheetState.Edit

    ModalBottomSheet(
        onDismissRequest = { onAction(CategoryEditUiAction.OnSheetCancelClick) },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                text = if (isEditMode) "カテゴリを編集" else "カテゴリを追加",
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.editingCategoryName,
                onValueChange = { value ->
                    onAction(CategoryEditUiAction.OnEditingNameChange(value))
                },
                label = { Text("カテゴリ名") },
                singleLine = true,
                isError = uiState.isEditingNameError,
                modifier = Modifier.fillMaxWidth(),
            )

            if (uiState.isEditingNameError) {
                Text(
                    text = "カテゴリ名を入力してください",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { onAction(CategoryEditUiAction.OnSheetCancelClick) },
                ) {
                    Text("キャンセル")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onAction(CategoryEditUiAction.OnSheetConfirmClick) },
                ) {
                    Text("保存")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
