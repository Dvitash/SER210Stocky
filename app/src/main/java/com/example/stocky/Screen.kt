package com.example.stocky

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Search : Screen("search")
    object Detail : Screen("detail") {
        const val ARG_STOCK_SYMBOL = "symbol"
        val routeWithArgument = "detail/{$ARG_STOCK_SYMBOL}"
    }
}
