package com.topdownedge.presentation.navigation

import androidx.navigation.NavHostController
import com.topdownedge.domain.entities.NewsArticle
import com.topdownedge.domain.entities.UserPosition
import com.topdownedge.domain.entities.common.Ticker
import kotlinx.serialization.Serializable

sealed interface ScreenDestination {

    @Serializable object Welcome : ScreenDestination

    @Serializable object HomeScreen : ScreenDestination

    @Serializable object Markets : ScreenDestination

    @Serializable object Portfolio : ScreenDestination

    @Serializable object News : ScreenDestination

    @Serializable data class NewsDetails(val newsTitle: String, val newsContent: String) : ScreenDestination

    @Serializable data class SearchAsset(val isForNewTrade: Boolean) : ScreenDestination

    @Serializable data class SubmitTrade(val tickerCode: String, val tickerExchange: String, val tickerName: String) : ScreenDestination

    @Serializable data class CompanyDetails(val tickerCode: String, val tickerExchange: String, val tickerName: String) : ScreenDestination

    @Serializable data class UserPosition(val tickerCode: String, val tickerExchange: String) : ScreenDestination
}

fun NavHostController.navigateToHomeScreen(){
    this.navigateClearingBackStack(ScreenDestination.HomeScreen)
}

fun NavHostController.navigateToSearchAssetScreen(isForNewTrade: Boolean) {
    this.navigateSingleTopTo(
        destinationRoute = ScreenDestination.SearchAsset(isForNewTrade)
    )
}

fun NavHostController.navigateToUserPositionScreen(position: UserPosition) {
    this.navigateSingleTopTo(
        destinationRoute = ScreenDestination.UserPosition(
            position.tickerCode,
            position.tickerExchange
        )
    )
}

fun NavHostController.navigateToSubmitTradeScreen(ticker: Ticker) {
    this.navigateSingleTopTo(
        destinationRoute = ScreenDestination.SubmitTrade(
            ticker.code,
            ticker.exchange,
            ticker.name
        ),
        saveState = false
    )
}

fun NavHostController.navigateToCompanyDetailsScreen(ticker: Ticker) {
    this.navigateSingleTopTo(
        destinationRoute = ScreenDestination.CompanyDetails(
            ticker.code,
            ticker.exchange,
            ticker.name
        ),
        saveState = false
    )
}

fun NavHostController.navigateToNewsDetailsScreen(newsArticle: NewsArticle) {
    this.navigateSingleTopTo(
        destinationRoute = ScreenDestination.NewsDetails(
            newsArticle.title,
            newsArticle.content
        )
    )
}

