package com.topdownedge.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class AuthResponse(
    val status_code: Int,
    val message: String
){
    companion object {
        fun parse(text: String): AuthResponse? = try {
            json.decodeFromString<AuthResponse>(text)
        } catch (e: Exception) {
            null
        }
    }
}

@Serializable
data class LivePriceDto(
    @SerialName("s") val symbol: String,
    @SerialName("p") val price: Double,
    @SerialName("ms") val marketStatus: String
) {
    companion object {
        fun parse(text: String): LivePriceDto? = try {
            json.decodeFromString<LivePriceDto>(text)
        } catch (e: Exception) {
            null
        }
    }
}