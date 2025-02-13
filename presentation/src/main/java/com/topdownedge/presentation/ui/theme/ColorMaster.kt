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


object ColorMaster {
    val priceRed
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.customColorsPalette.priceRed
    val priceGreen
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.customColorsPalette.priceGreen
    val primary
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.primary
    val tertiary
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.tertiary




//https://colormagic.app/palette/67aa0c31079f8ec4becb2cd9
    val pieChart1 = Color(0xFF3c9090)
    val pieChart2 = Color(0xFF6fbe8c)
    val pieChart3 = Color(0xFF88d8b0)
    val pieChart4 = Color(0xFFffcb5c)
    val pieChart5 = Color(0xFFff6e61)
    val pieChart6 = Color(0xFFff9f75)
    val pieChart7 = Color(0xFFffe1a8)
    val pieChart8 = Color(0xFF74c4d8)
    val pieChart9 = Color(0xFFe2c846)
    val pieChart10 = Color(0xFFe99516)
    val pieChart11 = Color(0xFF577925)
    val pieChart12 = Color(0xFF74c4d8)
    val pieChart13 = Color(0xFFf9e24e)
    val pieChart14 = Color(0xFFf6b53c)
    val pieChart15 = Color(0xFF4d4d89)

    val pieColorList = listOf(
        pieChart1,
        pieChart2,
        pieChart3,
        pieChart4,
        pieChart5,
        pieChart6,
        pieChart7,
        pieChart8,
        pieChart9,
        pieChart10,
        pieChart11,
        pieChart12,
        pieChart13,
        pieChart14,
        pieChart15,
    )

    fun getColor(index: Int): Color {
        return pieColorList[index % pieColorList.size]

    }




}