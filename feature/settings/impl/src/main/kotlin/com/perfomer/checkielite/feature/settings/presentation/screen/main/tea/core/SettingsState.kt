package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import java.util.Locale

internal data class SettingsState(
    val isSyncingInProgress: Boolean = false,
    val hasReviews: Boolean = false,
    val isCheckUpdatesInProgress: Boolean = false,
    val currentLocale: Locale = Locale.getDefault(),
    val currentTheme: ThemeMode = ThemeMode.SYSTEM,
    val isLiquidGlassEnabled: Boolean = true,
    val isLiquidGlassChangeInProgress: Boolean = false,
)
