package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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