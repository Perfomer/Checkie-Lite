package com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeState

internal class ThemeUiStateMapper(
    private val context: Context,
) : UiStateMapper<ThemeState, ThemeUiState> {

    override fun map(state: ThemeState): ThemeUiState {
        return ThemeUiState(
            items = state.themeOptions.map { option ->
                option.toUi(isSelected = state.currentTheme == option)
            },
        )
    }

    private fun ThemeMode.toUi(isSelected: Boolean): ThemeOption {
        return ThemeOption(
            type = this,
            icon = icon,
            text = context.getString(label),
            isSelected = isSelected,
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