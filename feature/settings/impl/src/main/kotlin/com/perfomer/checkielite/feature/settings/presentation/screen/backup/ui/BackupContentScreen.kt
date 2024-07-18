package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class BackupContentScreen(
    private val params: BackupParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<BackupStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        BackupScreen(
            state = state,
        )
    }
}