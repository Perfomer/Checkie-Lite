package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState

internal class SettingsUiStateMapper(
    private val context: Context,
) : UiStateMapper<SettingsState, SettingsUiState> {

    override fun map(state: SettingsState): SettingsUiState {
        return SettingsUiState(isLoading = false)
    }
}