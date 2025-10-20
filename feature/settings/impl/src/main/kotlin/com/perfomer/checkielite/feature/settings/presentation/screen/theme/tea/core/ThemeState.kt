package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal data class ThemeState(
    val currentTheme: ThemeMode,
    val themeOptions: List<ThemeMode> = ThemeMode.entries
)