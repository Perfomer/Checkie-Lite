package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.backup.BackupException
import com.perfomer.checkielite.core.domain.entity.backup.BackupMode
import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.Await
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.CancelBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.LaunchAppUpdate
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
                        effects(
                            ShowToast(
                                text = Text.resource(R.string.settings_backup_success_export),
                                style = ToastStyle.SUCCESS,
                            ),
                        )
                        commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.OPEN_MAIN))
                    }
                    BackupMode.IMPORT -> {
                        commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.RESTART))
                    }
                }
            }
            is BackupProgress.Cancelled -> {
                val text = when (state.mode) {
                    BackupMode.EXPORT -> Text.resource(R.string.settings_backup_cancel_export)
                    BackupMode.IMPORT -> Text.resource(R.string.settings_backup_cancel_import)
                }

                state { copy(isCancelled = true) }
                effects(ShowToast(text = text, style = ToastStyle.NEUTRAL))
                commands(Await(durationMs = DELAY_AFTER_FINISH_MS, reason = Await.Reason.OPEN_MAIN))
            }
            is BackupProgress.Failure -> {
                val text = when {
                    progress.error.message?.contains(NO_SPACE_MESSAGE) == true -> {
                        Text.resource(R.string.settings_backup_failure_common_no_space)
                    }
                    progress.error is BackupException.DatabaseVersionNotSupported -> {
                        Text.resource(R.string.settings_backup_failure_import_need_update)
                    }
                    state.mode == BackupMode.EXPORT -> {
                        Text.resource(R.string.settings_backup_failure_export)
                    }
                    state.mode == BackupMode.IMPORT -> {
                        Text.resource(R.string.settings_backup_failure_import)
                    }
                    else -> null
                }

                effects(text?.let { ShowToast(text = it, style = ToastStyle.ERROR) })

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
