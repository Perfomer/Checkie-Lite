package com.perfomer.checkielite.common.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CheckieLiteTheme(
    content: @Composable () -> Unit,
) {
    TransparentSystemBars()

    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        content = { CompositionLocalContent(content) },
    )
}

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
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


@Composable
fun TransparentSystemBars() {
    val activity = LocalContext.current as Activity
    val systemUiController = rememberSystemUiController()

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        systemUiController.setNavigationBarColor(
            color = Color.White.copy(alpha = 0.01F),
            darkIcons = true,
        )

        systemUiController.setStatusBarColor(
            color = Color.White.copy(alpha = 0.01F),
            darkIcons = true,
        )
    }
}

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