package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text

@Immutable
internal data class SettingsUiState(
    val appVersion: Text,
    val isCheckUpdatesInProgress: Boolean,
    val currentLanguage: Text,
    @DrawableRes val themeIcon: Int,
    val themeMode: Text,
)
