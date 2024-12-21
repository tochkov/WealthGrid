package com.topdownedge.domain.entities

sealed class NewsCategory(val title: String) {

    object General: NewsCategory("General")
    data class Ticker(val ticker: String): NewsCategory(ticker)
    data class Topic(val topic: String): NewsCategory(topic)

}