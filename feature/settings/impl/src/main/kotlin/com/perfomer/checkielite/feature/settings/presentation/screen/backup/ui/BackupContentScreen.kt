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
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast.Reason
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

        val noSpaceLeftToast = rememberErrorToast(R.string.settings_backup_failure_common_no_space)
        val importFailedCommonToast = rememberErrorToast(R.string.settings_backup_failure_import)
        val importFailedUpdateRequiredToast = rememberErrorToast(R.string.settings_backup_failure_import_need_update)
        val importCancelledToast = rememberToast(R.string.settings_backup_cancel_import)
        val exportFailedCommonToast = rememberErrorToast(R.string.settings_backup_failure_export)
        val exportSucceedToast = rememberSuccessToast(R.string.settings_backup_success_export)
        val exportCancelledToast = rememberToast(R.string.settings_backup_cancel_export)

        EffectHandler { effect ->
            when (effect) {
                is ShowToast -> toastHostState.showToast(
                    when (effect.reason) {
                        Reason.EXPORT_SUCCESS -> exportSucceedToast
                        Reason.IMPORT_CANCELLED -> importCancelledToast
                        Reason.EXPORT_CANCELLED -> exportCancelledToast
                        Reason.BACKUP_FAILED_NO_SPACE -> noSpaceLeftToast
                        Reason.EXPORT_FAILED_COMMON -> exportFailedCommonToast
                        Reason.IMPORT_FAILED_COMMON -> importFailedCommonToast
                        Reason.IMPORT_FAILED_UPDATE_REQUIRED -> importFailedUpdateRequiredToast
                    }
                )
            }
        }

        BackupScreen(
            state = state,
            onCancelClick = acceptable(OnCancelClick),
        )
    }
}