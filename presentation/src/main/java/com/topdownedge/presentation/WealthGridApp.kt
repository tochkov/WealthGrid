package com.topdownedge.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.topdownedge.presentation.navigation.ScreenDestination
import com.topdownedge.presentation.navigation.WealthGridHomeScreen
import com.topdownedge.presentation.navigation.navigateClearingBackStack
import com.topdownedge.presentation.welcome.WelcomeScreen

@Composable
fun WealthGridApp(
    hasApiToken: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (!hasApiToken) ScreenDestination.Welcome else ScreenDestination.WealthGridHome
    ) {
        composable<ScreenDestination.Welcome> {
            WelcomeScreen(
                onSuccessfulClickNext = {
                    navController.navigateClearingBackStack(ScreenDestination.WealthGridHome)
                }
            )
        }
        composable<ScreenDestination.WealthGridHome> {
            WealthGridHomeScreen()
        }
    }

}
