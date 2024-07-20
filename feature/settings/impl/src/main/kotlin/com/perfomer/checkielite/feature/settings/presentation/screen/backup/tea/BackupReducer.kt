package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.AwaitCompleted
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Backup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.OpenMain
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.RestartApp
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress

internal class BackupReducer : DslReducer<BackupCommand, BackupEffect, BackupEvent, BackupState>() {

    override fun reduce(event: BackupEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is BackupUiEvent -> reduceUi(event)
        is Backup -> reduceBackup(event)
        is AwaitCompleted -> reduceAwaitCompleted()
    }

    private fun reduceInitialize() {
        commands(ObserveBackupProgress)
    }

    private fun reduceUi(event: BackupUiEvent) = when (event) {
        is OnBackPress -> Unit // We purposefully forbid the exit.
    }

    private fun reduceBackup(event: Backup) = when (event) {
        is Backup.ProgressUpdated -> {
            state { copy(progressValue = event.progress) }
        }
        is Backup.Completed -> {
            state { copy(progressValue = 1F) }
            commands(BackupCommand.Await(DELAY_AFTER_SUCCESS_MS))
        }
    }

    private fun reduceAwaitCompleted() = when (state.mode) {
        BackupMode.EXPORT -> commands(OpenMain)
        BackupMode.IMPORT -> commands(RestartApp)
    }

    private companion object {
        private const val DELAY_AFTER_SUCCESS_MS = 3_000L
    }
}