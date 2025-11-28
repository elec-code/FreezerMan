package com.yasumu.feature.category_edit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryEditRoute() {
    CategoryEditScreen()
}

@Composable
private fun CategoryEditScreen(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "CategoryEdit WIP",
        modifier = modifier,
    )
}
