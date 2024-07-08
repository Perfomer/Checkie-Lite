package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class SettingsContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<SettingsStore>()) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        SettingsScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
            onBackupExportClick = acceptable(OnBackupExportClick),
            onBackupImportClick = acceptable(OnBackupImportClick),
        )
    }
}