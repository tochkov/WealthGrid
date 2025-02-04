package com.topdownedge.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenDestination {

    @Serializable object Welcome : ScreenDestination

    @Serializable object HomeScreen : ScreenDestination

    @Serializable object Markets : ScreenDestination

    @Serializable object Portfolio : ScreenDestination

//    @Serializable object NewsGraph : ScreenDestination
    @Serializable object News : ScreenDestination
    @Serializable data class SingleNews(val newsTitle: String, val newsContent: String) : ScreenDestination

    @Serializable data class SearchAsset(val isForNowTrade: Boolean) : ScreenDestination

    @Serializable data class NewTrade(val tickerCode: String, val tickerExchange: String, val tickerName: String) : ScreenDestination

    @Serializable data class CompanyDetails(val tickerCode: String, val tickerExchange: String, val tickerName: String) : ScreenDestination

    @Serializable data class UserPosition(val tickerCode: String, val tickerExchange: String) : ScreenDestination
}

