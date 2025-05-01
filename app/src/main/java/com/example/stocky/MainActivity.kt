package com.example.stocky

import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.stocky.screens.MainScreen
import com.example.stocky.screens.SplashScreen
import com.example.stocky.ui.theme.StockyTheme
import com.example.stocky.screens.DeveloperInfoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockyApp()
        }
    }
}

@Composable
fun StockyNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Main.route) {
            MainScreen(
                onStockClick = { symbol ->
                    navController.navigate("${Screen.Detail.route}/$symbol")
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToDeveloperInfo = {
                    navController.navigate(Screen.DeveloperInfo.route)
                },
                onToggleDarkMode = onToggleDarkMode,
                isDarkMode = isDarkMode
            )
        }

        composable(route = Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Detail.routeWithArgument) { navBackStackEntry ->
            val symbol = navBackStackEntry.arguments?.getString("symbol").orEmpty()
            DetailScreen(
                stockSymbol = symbol,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.DeveloperInfo.route) {
            DeveloperInfoScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview
@Composable
fun StockyApp() {
    val navController = rememberNavController()
    var isDarkMode by remember { mutableStateOf(false) }

    StockyTheme(darkTheme = isDarkMode, dynamicColor = false) {
        Surface {
            StockyNavGraph(
                navController = navController,
                isDarkMode = isDarkMode,
                onToggleDarkMode = { isDarkMode = !isDarkMode }
            )
        }
    }
}