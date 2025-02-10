package com.topdownedge.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorsPalette(
//    val custom1OnBackground: Color = Color.Unspecified,
//    val custom2OnBackground: Color = Color.Unspecified,
//    val custom1OnSurface: Color = Color.Unspecified,
//    val custom2OnSurface: Color = Color.Unspecified,
    val priceRed: Color = Color.Unspecified,
    val priceGreen: Color = Color.Unspecified,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }


val OnLightCustomColorsPalette = CustomColorsPalette(
//    custom1OnBackground = Color(color = 0xFF1A237E),
//    custom2OnBackground = Color(color = 0xFF1B5E20),
//    custom1OnSurface = Color(color = 0xFFE53935),
//    custom2OnSurface = Color(color = 0xFFD81B60),
    priceRed = Color(color = 0xFFF23645),
    priceGreen = Color(color = 0xFF0AC64B),
//    cardTest = Color(0xFFEDF1F1),
)

val OnDarkCustomColorsPalette = CustomColorsPalette(
//    custom1OnBackground = Color(color = 0xFF1E88E5),
//    custom2OnBackground = Color(color = 0xFF43A047),
//    custom1OnSurface = Color(color = 0xFFC62828),
//    custom2OnSurface = Color(color = 0xFFAD1457),
    priceRed = Color(color = 0xFFF23645),
    priceGreen = Color(color = 0xFF0AC64B),
)

val MaterialTheme.customColorsPalette: CustomColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorsPalette.current


object CustomColors {
    val priceRed
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.tertiary

}