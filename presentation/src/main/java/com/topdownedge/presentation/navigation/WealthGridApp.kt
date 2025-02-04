package com.topdownedge.presentation.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.topdownedge.presentation.market.CompanyDetailsScreen
import com.topdownedge.presentation.news.NewsDetailsScreen
import com.topdownedge.presentation.portfolio.UserPositionScreen
import com.topdownedge.presentation.portfolio.trade.SearchAssetScreen
import com.topdownedge.presentation.portfolio.trade.SubmitTradeScreen
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

                                is ScreenDestination.SearchAsset -> {
                                    navController.navigateSingleTopTo(screen)
                                }

                                is ScreenDestination.CompanyDetails -> {
                                    navController.navigateSingleTopTo(screen)
                                }

                                is ScreenDestination.UserPosition -> {
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


                composable<ScreenDestination.SearchAsset>(
                    enterTransition = EnterFromBottomTransition,
                    popExitTransition = ExitToBottomTransition,
                ) { backstackEntry ->
                    val searchAsset: ScreenDestination.SearchAsset = backstackEntry.toRoute()
                    SearchAssetScreen(
                        onBackPress = {
                            navController.popBackStack()
                        },
                        onListItemClick = { ticker ->
                            navController.navigateSingleTopTo(
                                destinationRoute = if (searchAsset.isForNowTrade) {
                                    ScreenDestination.NewTrade(
                                        ticker.code,
                                        ticker.exchange,
                                        ticker.name
                                    )
                                } else {
                                    ScreenDestination.CompanyDetails(
                                        ticker.code,
                                        ticker.exchange,
                                        ticker.name
                                    )
                                },
                                saveState = false
                            )
                        }
                    )
                }

                composable<ScreenDestination.NewTrade> { backstackEntry ->
                    val ticker: ScreenDestination.NewTrade = backstackEntry.toRoute()
                    SubmitTradeScreen(
                        ticker.tickerCode,
                        ticker.tickerExchange,
                        ticker.tickerName,
                        onBackPress = {
                            navController.popBackStack()
                        }
                    )
                }

                composable<ScreenDestination.CompanyDetails> { backstackEntry ->
                    val ticker: ScreenDestination.CompanyDetails = backstackEntry.toRoute()
                    CompanyDetailsScreen(
                        ticker.tickerCode,
                        ticker.tickerExchange,
                        ticker.tickerName,
                    )
                }

                composable<ScreenDestination.UserPosition> { backstackEntry ->
                    val ticker: ScreenDestination.UserPosition = backstackEntry.toRoute()
                    UserPositionScreen(
                        ticker.tickerCode,
                        ticker.tickerExchange,
                    )
                }


            }
        }
    }
}
