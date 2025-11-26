package com.yasumu.feature.stock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StockListRoute(
    viewModel: StockListViewModel = viewModel(factory = StockListViewModelFactory()),
) {
    val uiState by viewModel.uiState.collectAsState()
    StockListScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    uiState: StockListUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FreezerMan") }
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
                    Divider()
                }
            }
        }
    }
}
