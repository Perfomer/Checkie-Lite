package com.perfomer.checkielite.common.ui.presentation.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

/** Surface colors shared by the main screen and its flows. */
object CuiSurfaceStyle {
    val background: Color
        @Composable
        @ReadOnlyComposable
        get() = with(LocalCuiPalette.current) { lerp(BackgroundPrimary, BackgroundAccentTertiary, 0.4F) }

    val ripple: Color
        @Composable
        @ReadOnlyComposable
        get() = lerp(LocalCuiPalette.current.BackgroundAccentPrimary, Color.White, 0.5F)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuiSurfaceContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(color = CuiSurfaceStyle.ripple),
        content = content,
    )
}
