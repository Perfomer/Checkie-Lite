package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.pure.util.capitalize
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState

internal class SettingsUiStateMapper(
    private val context: Context,
) : UiStateMapper<SettingsState, SettingsUiState> {

    override fun map(state: SettingsState): SettingsUiState {
        val currentLocale = state.currentLocale

        return SettingsUiState(
            appVersion = Text.raw(AppInfo.versionName),
            isCheckUpdatesInProgress = state.isCheckUpdatesInProgress,
            currentLanguage = Text.raw(currentLocale.getDisplayLanguage(currentLocale).capitalize()),
            themeIcon = state.currentTheme.icon,
            themeMode = Text.resource(state.currentTheme.label),
        )
    }

    private companion object {

        @get:DrawableRes
        private val ThemeMode.icon: Int
            get() = when (this) {
                ThemeMode.SYSTEM -> R.drawable.ic_theme_system
                ThemeMode.LIGHT -> R.drawable.ic_theme_light
                ThemeMode.DARK -> R.drawable.ic_theme_dark
            }

        @get:StringRes
        private val ThemeMode.label: Int
            get() = when (this) {
                ThemeMode.SYSTEM -> R.string.settings_theme_option_system
                ThemeMode.LIGHT -> R.string.settings_theme_option_light
                ThemeMode.DARK -> R.string.settings_theme_option_dark
            }
    }
}