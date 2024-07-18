package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupProgressUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent.OnBackPress

internal class BackupReducer : DslReducer<BackupCommand, BackupEffect, BackupEvent, BackupState>() {

    override fun reduce(event: BackupEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is BackupUiEvent -> reduceUi(event)
        is BackupProgressUpdated -> reduceBackupProgressUpdated(event)
    }

    private fun reduceInitialize() {
        commands(ObserveBackupProgress)
    }

    private fun reduceUi(event: BackupUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }

    private fun reduceBackupProgressUpdated(event: BackupProgressUpdated) {
        state { copy(progress = event.progress) }
    }
}