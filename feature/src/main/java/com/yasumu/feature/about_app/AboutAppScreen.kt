package com.yasumu.feature.about_app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AboutAppRoute() {
    AboutAppScreen()
}

@Composable
private fun AboutAppScreen(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "About WIP",
        modifier = modifier,
    )
}
