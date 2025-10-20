package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal data class SettingsState(
    val isSyncingInProgress: Boolean = false,
    val hasReviews: Boolean = false,
    val isCheckUpdatesInProgress: Boolean = false,
    val currentTheme: ThemeMode = ThemeMode.SYSTEM,
)