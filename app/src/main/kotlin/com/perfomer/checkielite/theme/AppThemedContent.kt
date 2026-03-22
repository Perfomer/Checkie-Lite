package com.perfomer.checkielite.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.perfomer.checkielite.core.theme.manager.ThemeManager

@Composable
fun AppThemedContent(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val themeMode by themeManager.themeMode.collectAsState()

    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    CheckieLiteTheme(
        darkTheme = darkTheme,
        content = content
    )
}