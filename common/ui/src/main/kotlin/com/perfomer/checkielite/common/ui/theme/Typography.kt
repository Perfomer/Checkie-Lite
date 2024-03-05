package com.perfomer.checkielite.common.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.perfomer.checkielite.common.ui.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val googleSansFont = GoogleFont("Google Sans")

internal val googleSansFontFamily = FontFamily(
    Font(
        googleFont = googleSansFont,
        fontProvider = provider,
        weight = FontWeight.Normal,
    ),
    Font(
        googleFont = googleSansFont,
        fontProvider = provider,
        weight = FontWeight.Medium,
    ),
    Font(
        googleFont = googleSansFont,
        fontProvider = provider,
        weight = FontWeight.Bold,
    )
)

@Composable
@SuppressLint("ComposableNaming")
fun CheckieTypography(
    typography: Typography,
    fontFamily: FontFamily,
): Typography {
    return typography.copy(
        displayLarge = typography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = typography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = typography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = typography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = typography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = typography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = typography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = typography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = typography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = typography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = typography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = typography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = typography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = typography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = typography.labelSmall.copy(fontFamily = fontFamily),
    )
}