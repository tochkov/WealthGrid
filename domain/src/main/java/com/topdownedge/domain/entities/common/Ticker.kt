package com.topdownedge.domain.entities.common

data class Ticker(
    val code: String,
    val name: String,
    val exchange: String,
    val country: String = "",
    val currency: String = "",
    val type: String = "",
    val isin: String = "",
    val sector: String = "",
    val industry: String = "",
    val indexWeight: Double? = null,
): Asset
