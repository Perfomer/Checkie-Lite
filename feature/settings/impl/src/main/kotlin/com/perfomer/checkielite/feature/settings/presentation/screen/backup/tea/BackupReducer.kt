package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import android.util.Log
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.Await
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.CancelBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.AwaitCompleted
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupProgressUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.OpenMain
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.RestartApp
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnCancelClick

internal class BackupReducer : DslReducer<BackupCommand, BackupEffect, BackupEvent, BackupState>() {

    override fun reduce(event: BackupEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is BackupUiEvent -> reduceUi(event)
        is BackupProgressUpdated -> reduceBackupProgressUpdated(event)
        is AwaitCompleted -> reduceOnAwaitCompleted()
    }

    private fun reduceInitialize() {
        commands(ObserveBackupProgress)
    }

    private fun reduceUi(event: BackupUiEvent) = when (event) {
        is OnBackPress -> Unit // We purposefully forbid the exit.
        is OnCancelClick -> commands(CancelBackup)
    }

    private fun reduceBackupProgressUpdated(event: BackupProgressUpdated) {
        val progress = event.progress

        if (progress !is BackupProgress.None) {
            state { copy(backupProgress = progress) }
        }

        when (progress) {
            is BackupProgress.None -> {
                Unit
            }
            is BackupProgress.InProgress -> {
                state { copy(progressValue = progress.progress) }
            }
            is BackupProgress.Completed -> {
                state { copy(progressValue = 1F) }
                commands(Await(DELAY_AFTER_FINISH_MS))

                if (state.mode == BackupMode.EXPORT) {
                    effects(ShowToast.SuccessExport)
                }
            }
            is BackupProgress.Cancelled -> {
                state { copy(isCancelled = true) }
                effects(ShowToast.Cancelled)
                commands(Await(DELAY_AFTER_FINISH_MS))
            }
            is BackupProgress.Failure -> {
                val reason = when (state.mode) {
                    BackupMode.EXPORT -> {
                        if (progress.error.message?.contains(NO_SPACE_MESSAGE) == true) {
                            ShowToast.Error.Reason.EXPORT_FAILED_NO_SPACE
                        } else {
                            ShowToast.Error.Reason.EXPORT_FAILED_COMMON
                        }
                    }
                    BackupMode.IMPORT -> {
                        ShowToast.Error.Reason.IMPORT_FAILED_COMMON
                    }
                }

                Log.e("BackupReducer", "Failed to backup", progress.error)
                commands(Await(DELAY_AFTER_FINISH_MS))
                effects(ShowToast.Error(reason))
            }
        }
    }

    private fun reduceOnAwaitCompleted() {
        when (state.mode) {
            BackupMode.EXPORT -> {
                commands(OpenMain)
            }
            BackupMode.IMPORT -> {
                commands(RestartApp)
            }
        }
    }

    private companion object {
        private const val DELAY_AFTER_FINISH_MS = 2_000L
        private const val NO_SPACE_MESSAGE = "ENOSPC (No space left on device)"
    }
}