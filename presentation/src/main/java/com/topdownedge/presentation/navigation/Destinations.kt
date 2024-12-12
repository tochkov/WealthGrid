package com.topdownedge.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenDestination {

    @Serializable object Welcome : ScreenDestination
    @Serializable object Welcome2 : ScreenDestination

    @Serializable object WealthGridHome : ScreenDestination

    @Serializable object MarketsGraph : ScreenDestination
    @Serializable object Markets : ScreenDestination

    @Serializable object PortfolioGraph : ScreenDestination
    @Serializable object Portfolio : ScreenDestination

    @Serializable object NewsGraph : ScreenDestination
    @Serializable object News : ScreenDestination
    @Serializable data class SingleNews(val newsId: String) : ScreenDestination

}

