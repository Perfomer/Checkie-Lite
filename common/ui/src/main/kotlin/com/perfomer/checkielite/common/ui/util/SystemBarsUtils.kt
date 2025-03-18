@file:Suppress("DEPRECATION")

package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowInsetsControllerCompat
import com.composables.core.LocalModalWindow
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun TransparentSystemBars(darkIcons: Boolean = !isSystemInDarkTheme()) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setTransparentSystemBars(darkIcons)
}

fun SystemUiController.setTransparentSystemBars(darkIcons: Boolean) {
    setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = darkIcons,
        transformColorForLightContent = { Color.Transparent },
    )

    isNavigationBarContrastEnforced = false
}

@Suppress("DEPRECATION")
@Composable
internal fun DialogTransparentNavBar() {
    val window = LocalModalWindow.current
    LaunchedEffect(Unit) {
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            isAppearanceLightNavigationBars = true
        }
    }
}