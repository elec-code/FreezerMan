package com.yasumu.feature.stock_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasumu.core.domain.stock.StockRepository

@Composable
fun StockListRoute(
    stockRepository: StockRepository,
    onNavigateToCategoryEdit: () -> Unit,
    onNavigateToLocationEdit: () -> Unit,
    onNavigateToAboutApp: () -> Unit,
    viewModel: StockListViewModel = viewModel(
        factory = StockListViewModelFactory(stockRepository),
    ),
) {
    val uiState by viewModel.uiState.collectAsState()
    StockListScreen(
        uiState = uiState,
        onNavigateToCategoryEdit = onNavigateToCategoryEdit,
        onNavigateToLocationEdit = onNavigateToLocationEdit,
        onNavigateToAboutApp = onNavigateToAboutApp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    uiState: StockListUiState,
    modifier: Modifier = Modifier,
    onNavigateToCategoryEdit: () -> Unit = {},
    onNavigateToLocationEdit: () -> Unit = {},
    onNavigateToAboutApp: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            var menuExpanded by remember { mutableStateOf(false) }

            TopAppBar(
                title = { Text("FreezerMan") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "メニュー",
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("カテゴリ編集") },
                            onClick = {
                                menuExpanded = false
                                onNavigateToCategoryEdit()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("保管場所編集") },
                            onClick = {
                                menuExpanded = false
                                onNavigateToLocationEdit()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text("FreezerMan について") },
                            onClick = {
                                menuExpanded = false
                                onNavigateToAboutApp()
                            },
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 追加画面への遷移は後で */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "追加")
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                items(uiState.stocks, key = { it.id.value }) { stock ->
                    ListItem(
                        headlineContent = { Text(stock.name) },
                        supportingContent = {
                            Text(
                                text = "残り ${stock.quantity} ／ 期限 ${stock.bestBeforeDate}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
