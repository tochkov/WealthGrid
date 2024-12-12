package com.topdownedge.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.topdownedge.presentation.welcome.WelcomeScreen
import com.topdownedge.presentation.welcome.WelcomeScreen2

@Composable
fun WealthGridApp() {

    val navController = rememberNavController()
    val isLoggedIn = true

    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) ScreenDestination.Welcome else ScreenDestination.WealthGridHome
    ) {
        composable<ScreenDestination.Welcome> {
            WelcomeScreen(
                onClickNext = { navController.navigateSingleTopTo(ScreenDestination.Welcome2) }
            )
        }
        composable<ScreenDestination.Welcome2> {
            WelcomeScreen2(
                onClickNext = { navController.navigateClearingBackStack(ScreenDestination.WealthGridHome) }
            )
        }
        composable<ScreenDestination.WealthGridHome> {
            WealthGridHomeScreen()
        }
    }

}
