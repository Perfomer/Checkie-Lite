package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState

internal class BackupUiStateMapper(
    private val context: Context,
) : UiStateMapper<BackupState, BackupUiState> {

    override fun map(state: BackupState): BackupUiState {
        val progressPercent = (state.progressValue * 100).toInt()

        return BackupUiState(
            title = when (state.mode) {
                BackupMode.IMPORT -> context.getString(R.string.settings_backup_title_import)
                BackupMode.EXPORT -> context.getString(R.string.settings_backup_title_export)
            },
            backupProgress = state.progressValue,
            progressLabel = "$progressPercent%",
            isCancelAvailable = state.backupProgress is BackupProgress.InProgress,
            progressBarStyle = when (state.backupProgress) {
                is BackupProgress.None,
                is BackupProgress.InProgress -> BackupProgressBarStyle.IN_PROGRESS
                is BackupProgress.Completed -> BackupProgressBarStyle.COMPLETED
                is BackupProgress.Cancelled -> BackupProgressBarStyle.CANCELLED
                is BackupProgress.Failure -> BackupProgressBarStyle.FAILED
            }
        )
    }
}