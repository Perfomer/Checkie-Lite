package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal data class SettingsState(
    val isSyncingInProgress: Boolean = false,
    val hasReviews: Boolean = false,
    val isCheckUpdatesInProgress: Boolean = false,
)