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
    val masterNavController = rememberNavController()
    WealthGridTheme {
        Surface {
            NavHost(
                navController = masterNavController,
                startDestination = if (!hasApiToken) ScreenDestination.Welcome else ScreenDestination.HomeScreen
            ) {
                composable<ScreenDestination.Welcome> {
                    WelcomeScreen(masterNavController)
                }

                composable<ScreenDestination.HomeScreen> {
                    WealthGridHomeScreen(masterNavController)
                }

                composable<ScreenDestination.NewsDetails>(
                    enterTransition = EnterFromBottomTransition,
                    exitTransition = ExitToBottomTransition
                ) { backstackEntry ->
                    val singleNews: ScreenDestination.NewsDetails = backstackEntry.toRoute()
                    NewsDetailsScreen(singleNews.newsTitle, singleNews.newsContent)
                }

                composable<ScreenDestination.SearchAsset>(
                    enterTransition = EnterFromBottomTransition,
                    popExitTransition = ExitToBottomTransition,
                ) { backstackEntry ->
                    val searchAsset: ScreenDestination.SearchAsset = backstackEntry.toRoute()
                    SearchAssetScreen(masterNavController, searchAsset.isForNewTrade)
                }

                composable<ScreenDestination.SubmitTrade> { backstackEntry ->
                    val company: ScreenDestination.SubmitTrade = backstackEntry.toRoute()
                    SubmitTradeScreen(
                        company.tickerCode,
                        company.tickerExchange,
                        company.tickerName,
                        onBackPress = {
                            masterNavController.popBackStack()
                        }
                    )
                }

                composable<ScreenDestination.CompanyDetails> { backstackEntry ->
                    val company: ScreenDestination.CompanyDetails = backstackEntry.toRoute()
                    CompanyDetailsScreen(
                        masterNavController,
                        company.tickerCode,
                        company.tickerExchange,
                        company.tickerName,
                    )
                }

                composable<ScreenDestination.UserPosition> { backstackEntry ->
                    val position: ScreenDestination.UserPosition = backstackEntry.toRoute()
                    UserPositionScreen(
                        masterNavController,
                        position.tickerCode,
                        position.tickerExchange,
                    )
                }


            }
        }
    }
}
