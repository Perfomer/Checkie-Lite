package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberToast
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class BackupContentScreen(
    private val params: BackupParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<BackupStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        val toastHostState = LocalCuiToastHostState.current
        val exportFailedToast = rememberToast(message = R.string.settings_backup_failure_export, icon = CommonDrawable.ic_error)
        val importFailedToast = rememberToast(message = R.string.settings_backup_failure_import, icon = CommonDrawable.ic_error)

        EffectHandler { effect ->
            when (effect) {
                is BackupEffect.ShowErrorToast -> when (effect.mode) {
                    BackupMode.EXPORT -> toastHostState.showToast(exportFailedToast)
                    BackupMode.IMPORT -> toastHostState.showToast(importFailedToast)
                }
            }
        }

        BackupScreen(
            state = state,
        )
    }
}