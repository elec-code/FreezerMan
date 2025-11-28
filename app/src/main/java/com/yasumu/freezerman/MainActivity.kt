package com.yasumu.freezerman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.yasumu.core.domain.repository.StockRepository
import com.yasumu.freezerman.navigation.FreezerManNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as FreezerManApplication).appContainer
        enableEdgeToEdge()
        setContent {
            FreezerManApp(stockRepository = appContainer.stockRepository)
        }
    }
}
@Composable
fun FreezerManApp(
    stockRepository: StockRepository,
) {
    val navController = rememberNavController()

    FreezerManNavHost(
        navController = navController,
        stockRepository = stockRepository,
    )
}