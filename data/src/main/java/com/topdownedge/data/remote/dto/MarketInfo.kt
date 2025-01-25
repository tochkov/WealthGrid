package com.topdownedge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class IndexResponse(
    val Components: Map<String, IndexConstituentDto>,
    val General: GeneralInfo
)

@Serializable
data class GeneralInfo(
    val Code: String,
    val CountryISO: String,
    val CountryName: String,
    val CurrencyCode: String,
    val CurrencyName: String,
    val CurrencySymbol: String,
    val Exchange: String,
    val MarketCap: Double,
    val Name: String,
    val Type: String
)

@Serializable
data class IndexConstituentDto(
    val Code: String,
    val Name: String,
    val Exchange: String,
    val Industry: String?,
    val Sector: String?,
    val Weight: Double?
)

@Serializable
data class SymbolItemDto(
    val Code: String,
    val Name: String,
    val Country: String,
    val Exchange: String,
    val Currency: String,
    val Type: String,
)
