package com.perfomer.checkielite.common.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
fun CheckieLiteTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        shapes = CheckieShapes,
        content = { CompositionLocalContent(content) },
    )
}

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        shapes = CheckieShapes,
        content = { CompositionLocalContent(content) },
    )
}

@Composable
private fun CompositionLocalContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(
//        LocalShimmerTheme provides BakemateShimmerTheme,
//        LocalRippleTheme provides BakemateRippleTheme,
        content = content,
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
)