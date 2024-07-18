package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState

internal class BackupUiStateMapper(
    private val context: Context,
) : UiStateMapper<BackupState, BackupUiState> {

    override fun map(state: BackupState): BackupUiState {
        return BackupUiState(
            title = "Backup",
            progressPercentDone = 100,
        )
    }
}