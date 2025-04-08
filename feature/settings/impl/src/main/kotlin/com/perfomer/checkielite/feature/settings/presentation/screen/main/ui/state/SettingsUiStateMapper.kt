package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.util.app.AppInfoHolder
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState


internal class SettingsUiStateMapper : UiStateMapper<SettingsState, SettingsUiState> {

    override fun map(state: SettingsState): SettingsUiState {
        return SettingsUiState(
            appVersion = AppInfoHolder.getAppInfo().versionName,
            isCheckUpdatesInProgress = state.isCheckUpdatesInProgress,
        )
    }
}