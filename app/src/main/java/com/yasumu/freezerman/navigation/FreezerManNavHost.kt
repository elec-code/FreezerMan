package com.yasumu.freezerman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yasumu.core.domain.category.CategoryRepository
import com.yasumu.core.domain.stock.StockRepository
import com.yasumu.feature.stock_list.StockListRoute
import com.yasumu.feature.category_edit.CategoryEditRoute
import com.yasumu.feature.location_edit.LocationEditRoute
import com.yasumu.feature.about_app.AboutAppRoute


@Composable
fun FreezerManNavHost(
    navController: NavHostController,
    stockRepository: StockRepository,
    categoryRepository: CategoryRepository,
) {
    NavHost(
        navController = navController,
        startDestination = FreezerManDestination.StockList.route,
    ) {
        composable(route = FreezerManDestination.StockList.route) {
            StockListRoute(
                stockRepository = stockRepository,
                onNavigateToCategoryEdit = {
                    navController.navigate(FreezerManDestination.CategoryEdit.route)
                },
                onNavigateToLocationEdit = {
                    navController.navigate(FreezerManDestination.LocationEdit.route)
                },
                onNavigateToAboutApp = {
                    navController.navigate(FreezerManDestination.About.route)
                },
            )
        }

        // ★今後のための TODO（1-B/1-C 以降で追加）
        //
        // composable(route = FreezerManDestination.StockAdd.route) { ... }
        //
        // composable(
        //     route = FreezerManDestination.StockEdit.routeWithArg,
        //     arguments = listOf(
        //         navArgument(FreezerManDestination.StockEdit.stockIdArg) {
        //             type = NavType.LongType
        //         },
        //     ),
        // ) { backStackEntry ->
        //     val stockId = backStackEntry.arguments
        //         ?.getLong(FreezerManDestination.StockEdit.stockIdArg)
        //         ?: error("stockId is required")
        //     StockEditRoute(stockId = stockId)
        // }
        //

        // ③ カテゴリ編集
        composable(route = FreezerManDestination.CategoryEdit.route) {
            CategoryEditRoute(
                categoryRepository = categoryRepository,
                stockRepository = stockRepository,
            )
        }

        // ④ 保管場所編集
        composable(route = FreezerManDestination.LocationEdit.route) {
            LocationEditRoute()
        }

        // ⑤ About 画面
        composable(route = FreezerManDestination.About.route) {
            AboutAppRoute()
        }
    }
}
