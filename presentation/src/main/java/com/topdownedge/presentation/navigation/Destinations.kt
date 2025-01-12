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

    @Serializable object TradeGraph : ScreenDestination
    @Serializable object TradeInitiation : ScreenDestination
    @Serializable object InstrumentPicker : ScreenDestination
}

