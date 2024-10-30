package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberWarningToast
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowConfirmImportDialog
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportConfirmClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.WarningReason
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class SettingsContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<SettingsStore>()) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        val toastController = LocalCuiToastHostState.current
        val syncingInProgressToast = rememberWarningToast(message = CommonString.common_toast_syncing)

        var shouldShowBackupImportConfirmDialog by remember { mutableStateOf(false) }

        EffectHandler { effect ->
            when (effect) {
                is ShowConfirmImportDialog -> shouldShowBackupImportConfirmDialog = true
                is ShowToast.Warning -> when (effect.reason) {
                    WarningReason.SYNCING_IN_PROGRESS -> toastController.showToast(syncingInProgressToast)
                }
            }
        }

        SettingsScreen(
            state = state,

            shouldShowBackupImportConfirmDialog = shouldShowBackupImportConfirmDialog,
            onBackupImportConfirmDialogConfirm = acceptable(OnBackupImportConfirmClick),
            onBackupImportConfirmDialogDismiss = { shouldShowBackupImportConfirmDialog = false },

            onNavigationIconClick = acceptable(OnBackPress),
            onBackupExportClick = acceptable(OnBackupExportClick),
            onBackupImportClick = acceptable(OnBackupImportClick),
        )
    }
}