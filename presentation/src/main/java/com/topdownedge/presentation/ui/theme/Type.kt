package com.topdownedge.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.topdownedge.presentation.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
//        googleFont = GoogleFont("Lato"),
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
//        googleFont = GoogleFont("Rajdhani"),
        googleFont = GoogleFont("JetBrains Mono"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.ExtraBold
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily
    ),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.ExtraBold
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFontFamily
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFontFamily
    ),
    // ---------------------------------------
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = bodyFontFamily
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = bodyFontFamily,
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = bodyFontFamily
    ),
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFontFamily
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFontFamily
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = bodyFontFamily
    ),
)
