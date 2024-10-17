package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberErrorToast
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberSuccessToast
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberToast
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnCancelClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class BackupContentScreen(
    private val params: BackupParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<BackupStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        val toastHostState = LocalCuiToastHostState.current

        val exportFailedCommonToast = rememberErrorToast(R.string.settings_backup_failure_export)
        val exportFailedNoSpaceToast = rememberErrorToast(R.string.settings_backup_failure_export_no_space)
        val importFailedCommonToast = rememberErrorToast(R.string.settings_backup_failure_import)
        val exportSucceedToast = rememberSuccessToast(R.string.settings_backup_success_export)
        val exportCancelledToast = rememberToast(R.string.settings_backup_cancel_export)

        EffectHandler { effect ->
            when (effect) {
                is ShowToast.Success -> {
                    toastHostState.showToast(exportSucceedToast)
                }
                is ShowToast.Cancelled -> {
                    toastHostState.showToast(exportCancelledToast)
                }
                is ShowToast.Error -> when (effect.reason) {
                    ShowToast.Error.Reason.EXPORT_FAILED_COMMON -> toastHostState.showToast(exportFailedCommonToast)
                    ShowToast.Error.Reason.EXPORT_FAILED_NO_SPACE -> toastHostState.showToast(exportFailedNoSpaceToast)
                    ShowToast.Error.Reason.IMPORT_FAILED_COMMON -> toastHostState.showToast(importFailedCommonToast)
                }
            }
        }

        BackupScreen(
            state = state,
            onCancelClick = acceptable(OnCancelClick),
        )
    }
}