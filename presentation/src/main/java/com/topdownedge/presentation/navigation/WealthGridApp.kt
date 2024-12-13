package com.topdownedge.presentation.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.topdownedge.presentation.welcome.WelcomeScreen

@Composable
fun WealthGridApp() {

    val navController = rememberNavController()

    val ctx = LocalContext.current
    val prefs = ctx.getSharedPreferences("wg_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = !prefs.getString("wg_token", "").isNullOrEmpty()

    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) ScreenDestination.Welcome else ScreenDestination.WealthGridHome
    ) {
        composable<ScreenDestination.Welcome> {
            WelcomeScreen(
                onClickNext = { tokenString ->
                    if (tokenString.isNotBlank()) {
                        with(prefs.edit()) {
                            putString("wg_token", tokenString)
                            apply()
                        }
                        navController.navigateClearingBackStack(ScreenDestination.WealthGridHome)
                    } else {
                        Toast.makeText(ctx, "Please enter token", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        composable<ScreenDestination.WealthGridHome> {
            WealthGridHomeScreen()
        }
    }

}
