package com.topdownedge.presentation.common

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun randomColor() = Color(
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256)
)

fun getLogoUrl(tickerCode: String) =
    "https://assets.parqet.com/logos/symbol/$tickerCode?format=jpg&size=100"
//"https://financialmodelingprep.com/image-stock/$tickerCode.png"
//"https://eodhd.com/img/logos/US/${tickerCode.lowercase()}.png"

fun Color.isFromCache(isFromCache: Boolean) = if (isFromCache) this.copy(alpha = 0.4f) else this

fun Color.applyAlpha(alpha: Float) = this.copy(alpha = alpha)
