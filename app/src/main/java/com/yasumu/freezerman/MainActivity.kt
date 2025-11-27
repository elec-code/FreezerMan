package com.yasumu.freezerman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.yasumu.feature.stock.StockListRoute
import com.yasumu.freezerman.ui.theme.FreezerManTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as FreezerManApplication).appContainer
        enableEdgeToEdge()
        setContent {
            FreezerManApp(appContainer)
        }
    }
}
@Composable
private fun FreezerManApp(appContainer: AppContainer) {
    FreezerManTheme {
        Surface {
            StockListRoute(
                stockRepository = appContainer.stockRepository,
            )
        }
    }
}