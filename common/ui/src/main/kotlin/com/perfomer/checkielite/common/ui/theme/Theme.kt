package com.perfomer.checkielite.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.R

@Composable
fun CheckieLiteTheme(
    content: @Composable () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()

    val colorScheme = if (isDarkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
    val palette = if (isDarkTheme) CuiPalette.Dark else CuiPalette.Light

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

    val fontName = GoogleFont("Google Sans")

    val fontFamily = FontFamily(
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.Normal,
        ),
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.Medium,
        ),
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.Bold,
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = CheckieShapes,
        typography = CheckieTypography(MaterialTheme.typography, googleSansFontFamily),
        content = {
            CompositionLocalProvider(
                LocalCuiPalette provides palette,
                content = content,
            )
        },
    )
}

@Deprecated(
    message = "Redundant theme. Use CheckieLiteTheme",
    replaceWith = ReplaceWith("CheckieLiteTheme"),
)
@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) = CheckieLiteTheme(content)

private val CheckieShapes = Shapes(
    extraSmall = RoundedCornerShape(16.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

private val LightAndroidColorScheme = lightColorScheme(
    primary = CuiPalette.Light.BackgroundAccentPrimary,
    onPrimary = CuiPalette.Light.BackgroundPrimary,
    secondary = CuiPalette.Light.TextSecondary,
    onSecondary = CuiPalette.Light.TextPrimary,
    background = CuiPalette.Light.BackgroundPrimary,
    onBackground = CuiPalette.Light.TextPrimary,
    surface = CuiPalette.Light.BackgroundPrimary,
    onSurface = CuiPalette.Light.TextPrimary,
    error = CuiPalette.Light.TextNegative,
    onError = CuiPalette.Light.BackgroundPrimary,
    primaryContainer = CuiPalette.Light.BackgroundAccentPrimary,
    onPrimaryContainer = CuiPalette.Light.BackgroundPrimary,
)

private val DarkAndroidColorScheme = darkColorScheme(
    primary = CuiPalette.Dark.BackgroundAccentPrimary,
    onPrimary = CuiPalette.Dark.BackgroundPrimary,
    secondary = CuiPalette.Dark.TextSecondary,
    onSecondary = CuiPalette.Dark.TextPrimary,
    background = CuiPalette.Dark.BackgroundPrimary,
    onBackground = CuiPalette.Dark.TextPrimary,
    surface = CuiPalette.Dark.BackgroundPrimary,
    onSurface = CuiPalette.Dark.TextPrimary,
    error = CuiPalette.Dark.TextNegative,
    onError = CuiPalette.Dark.BackgroundPrimary,
    primaryContainer = CuiPalette.Dark.BackgroundAccentPrimary,
    onPrimaryContainer = CuiPalette.Dark.BackgroundPrimary,
)