package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
internal data class SettingsUiState(
    val appVersion: String,
    val isCheckUpdatesInProgress: Boolean,
    @DrawableRes val themeIcon: Int,
    val themeMode: String,
)
