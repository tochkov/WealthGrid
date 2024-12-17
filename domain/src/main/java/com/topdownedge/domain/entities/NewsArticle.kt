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
