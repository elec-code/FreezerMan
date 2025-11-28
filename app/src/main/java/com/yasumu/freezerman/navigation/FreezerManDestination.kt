package com.yasumu.freezerman.navigation

/**
 * FreezerMan 内の全画面のルーティング情報。
 *
 * - route: NavHost に渡すルート文字列
 */
sealed interface FreezerManDestination {
    val route: String

    /**
     * ストック一覧画面
     */
    data object StockList : FreezerManDestination {
        override val route: String = "stock_list"
    }

    /**
     * ストック追加画面
     */
    data object StockAdd : FreezerManDestination {
        override val route: String = "stock_add"
    }

    /**
     * ストック編集画面
     *
     * 引数:
     * - stockId: Long
     */
    data object StockEdit : FreezerManDestination {
        override val route: String = "stock_edit"

        const val stockIdArg: String = "stockId"

        /**
         * NavHost に登録するルート文字列
         * 例: "stock_edit/{stockId}"
         */
        val routeWithArg: String = "$route/{$stockIdArg}"

        /**
         * navigate 時に使う実ルート
         * 例: createRoute(10) -> "stock_edit/10"
         */
        fun createRoute(stockId: Long): String = "$route/$stockId"
    }

    /**
     * カテゴリ編集画面
     */
    data object CategoryEdit : FreezerManDestination {
        override val route: String = "category_edit"
    }

    /**
     * 保管場所編集画面
     */
    data object LocationEdit : FreezerManDestination {
        override val route: String = "location_edit"
    }

    /**
     * About 画面
     */
    data object About : FreezerManDestination {
        override val route: String = "about"
    }
}
