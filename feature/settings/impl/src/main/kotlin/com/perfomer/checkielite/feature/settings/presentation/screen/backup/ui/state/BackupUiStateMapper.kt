package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.data.entity.BackupProgress
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.entity.BackupMode
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState

internal class BackupUiStateMapper(
    private val context: Context,
) : UiStateMapper<BackupState, BackupUiState> {

    override fun map(state: BackupState): BackupUiState {
        val progress = when (state.progress) {
            is BackupProgress.None -> 0F
            is BackupProgress.InProgress -> state.progress.progress
            is BackupProgress.Completed -> 1F
            is BackupProgress.Failure -> null
        }

        return BackupUiState(
            title = when (state.mode) {
                BackupMode.IMPORT -> context.getString(R.string.settings_backup_title_import)
                BackupMode.EXPORT -> context.getString(R.string.settings_backup_title_export)
            },
            backupProgress = progress,
            progressLabel = progress?.times(100)?.toInt()?.let { "$it%" },
        )
    }
}