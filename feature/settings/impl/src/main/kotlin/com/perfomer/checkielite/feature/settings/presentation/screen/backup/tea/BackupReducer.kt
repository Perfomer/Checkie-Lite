package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.domain.entity.backup.BackupException
import com.perfomer.checkielite.core.domain.entity.backup.BackupMode
import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.Await
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.CancelBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.LaunchAppUpdate
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast.Reason
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
        is AwaitCompleted -> reduceOnAwaitCompleted(event)
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

                when (state.mode) {
                    BackupMode.EXPORT -> {
                        effects(ShowToast(Reason.EXPORT_SUCCESS))
                        commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.OPEN_MAIN))
                    }
                    BackupMode.IMPORT -> {
                        commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.RESTART))
                    }
                }
            }
            is BackupProgress.Cancelled -> {
                val reason = when (state.mode) {
                    BackupMode.EXPORT -> Reason.EXPORT_CANCELLED
                    BackupMode.IMPORT -> Reason.IMPORT_CANCELLED
                }

                state { copy(isCancelled = true) }
                effects(ShowToast(reason))
                commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.OPEN_MAIN))
            }
            is BackupProgress.Failure -> {
                val reason = when {
                    progress.error.message?.contains(NO_SPACE_MESSAGE) == true -> {
                        Reason.BACKUP_FAILED_NO_SPACE
                    }
                    progress.error is BackupException.DatabaseVersionNotSupported -> {
                        Reason.IMPORT_FAILED_UPDATE_REQUIRED
                    }
                    state.mode == BackupMode.EXPORT -> {
                        Reason.EXPORT_FAILED_COMMON
                    }
                    state.mode == BackupMode.IMPORT -> {
                        Reason.IMPORT_FAILED_COMMON
                    }
                    else -> null
                }

                effects(reason?.let(::ShowToast))

                commands(
                    Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.OPEN_MAIN),
                    LaunchAppUpdate.takeIf { progress.error is BackupException.DatabaseVersionNotSupported },
                )
            }
        }
    }

    private fun reduceOnAwaitCompleted(event: AwaitCompleted) {
        when (event.reason) {
            Await.Reason.RESTART -> commands(RestartApp)
            Await.Reason.OPEN_MAIN -> commands(OpenMain)
        }
    }

    private companion object {
        private const val DELAY_AFTER_FINISH_MS = 2_000L
        private const val NO_SPACE_MESSAGE = "ENOSPC (No space left on device)"
    }
}