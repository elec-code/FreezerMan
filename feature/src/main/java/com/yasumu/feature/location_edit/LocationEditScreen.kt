package com.yasumu.feature.location_edit

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationEditRoute() {
    LocationEditScreen()
}

@Composable
private fun LocationEditScreen(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "LocationEdit WIP",
        modifier = modifier,
    )
}
