package com.yasumu.freezerman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.stock.StockRepository
import com.yasumu.freezerman.navigation.FreezerManNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as FreezerManApplication).appContainer
        enableEdgeToEdge()
        setContent {
            FreezerManApp(
                stockRepository = appContainer.stockRepository,
                categoryRepository = appContainer.categoryRepository,
            )
        }
    }
}

@Composable
fun FreezerManApp(
    stockRepository: StockRepository,
    categoryRepository: CategoryRepository,
) {
    val navController = rememberNavController()

    FreezerManNavHost(
        navController = navController,
        stockRepository = stockRepository,
        categoryRepository = categoryRepository,
    )
}
