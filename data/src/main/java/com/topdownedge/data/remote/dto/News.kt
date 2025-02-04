package com.topdownedge.data.remote.dto

import com.topdownedge.domain.entities.NewsArticle
import kotlinx.serialization.Serializable
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Data transfer object representing a news article from the EODHD API
 */
@Serializable
data class NewsArticleDto(
    val date: String,
    val title: String,
    val content: String,
    val link: String,
    val symbols: List<String>,
    val tags: List<String>,
    val sentiment: SentimentDto
)

/**
 * Data class representing sentiment analysis for a news article
 */
@Serializable
data class SentimentDto(
    val polarity: Double,
    val neg: Double,
    val neu: Double,
    val pos: Double
)

fun NewsArticleDto.toNewsArticle() = NewsArticle(
    date = getFormattedDate(date),
    title = title,
    content = content,
    url = link,
    source = getStrippedSource(url = link),
    symbols = symbols,
    tags = tags
)

fun getStrippedSource(url: String): String {
    var source = runCatching { URL(url).host }.getOrDefault("")
    if (source.startsWith("www.")) {
        source = source.substring(4)
    }
    return source
}

fun getFormattedDate(date: String): String {
    return LocalDate.parse(date.split("T")[0]).format(DateTimeFormatter.ofPattern("MMM dd"))
}
