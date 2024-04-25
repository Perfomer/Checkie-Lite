package com.perfomer.checkielite.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
fun CheckieLiteTheme(
    content: @Composable () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()

    val colorScheme = if (isDarkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
    val palette = if (isDarkTheme) CuiPalette.Dark else CuiPalette.Light

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = CheckieShapes,
        typography = CheckieTypography(MaterialTheme.typography, googleSansFontFamily),
        content = {
            CompositionLocalProvider(
                LocalCuiPalette provides palette,
                LocalContentColor provides MaterialTheme.colorScheme.onBackground,
                content = content,
            )
        },
    )
}

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
    outlineVariant = CuiPalette.Light.OutlineSecondary,
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
    outlineVariant = CuiPalette.Dark.OutlineSecondary,
)