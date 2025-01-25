package com.topdownedge.domain.entities

data class NewsArticle(
    val date: String,
    val title: String,
    val content: String,
    val url: String,
    val source: String,
    val symbols: List<String>,
    val tags: List<String>
)

sealed class NewsCategory(val title: String) {
    object General: NewsCategory("General")
    data class Ticker(val ticker: String): NewsCategory(ticker)
    data class Topic(val topic: String): NewsCategory(topic)
}
