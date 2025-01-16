package com.topdownedge.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.topdownedge.presentation.navigation.EnterFromBottomTransition
import com.topdownedge.presentation.navigation.ExitToBottomTransition
import com.topdownedge.presentation.navigation.ScreenDestination
import com.topdownedge.presentation.navigation.WealthGridHomeScreen
import com.topdownedge.presentation.navigation.navigateClearingBackStack
import com.topdownedge.presentation.navigation.navigateSingleTopTo
import com.topdownedge.presentation.news.NewsDetailsScreen
import com.topdownedge.presentation.portfolio.trade.AssetSearchScreen
import com.topdownedge.presentation.ui.theme.WealthGridTheme
import com.topdownedge.presentation.welcome.WelcomeScreen

@Composable
fun WealthGridApp(
    hasApiToken: Boolean
) {
    val navController = rememberNavController()
    WealthGridTheme {
        Surface {
            NavHost(
                navController = navController,
                startDestination = if (!hasApiToken) ScreenDestination.Welcome else ScreenDestination.HomeScreen
            ) {
                composable<ScreenDestination.Welcome> {
                    WelcomeScreen(
                        onSuccessfulClickNext = {
                            navController.navigateClearingBackStack(ScreenDestination.HomeScreen)
                        }
                    )
                }

                composable<ScreenDestination.HomeScreen> {
                    WealthGridHomeScreen(
                        onNavigateToScreen = { screen ->
                            when (screen) {
                                is ScreenDestination.SingleNews -> {
                                    navController.navigateSingleTopTo(screen)
                                }

                                is ScreenDestination.InstrumentPicker -> {
                                    navController.navigateSingleTopTo(screen)
                                }

                                else -> {}
                            }
                        }
                    )
                }

                composable<ScreenDestination.SingleNews>(
                    enterTransition = EnterFromBottomTransition,
                    exitTransition = ExitToBottomTransition
                ) { backstackEntry ->
                    val singleNews: ScreenDestination.SingleNews = backstackEntry.toRoute()
                    NewsDetailsScreen(singleNews.newsTitle, singleNews.newsContent)
                }

//                //TODO maybe extract this to new NavHost for the trade package; Or just rethink the flow
//                navigation<ScreenDestination.TradeGraph>(
//                    startDestination = ScreenDestination.TradeInitiation
//                ) {
//                    composable<ScreenDestination.TradeInitiation> {
//
//                        TradeInitiationScreen(
//                            navigateToInstrumentPicker = {
//                                navController.navigateDeeperTo(ScreenDestination.InstrumentPicker)
//                            }
//                        )
//                    }
//                    composable<ScreenDestination.InstrumentPicker> { backstackEntry ->
//                        InstrumentPickerScreen()
//                    }
//                }


                composable<ScreenDestination.InstrumentPicker>(
                    enterTransition = EnterFromBottomTransition,
                    exitTransition = ExitToBottomTransition
                ) {
                    AssetSearchScreen()
                }
            }
        }
    }
}
