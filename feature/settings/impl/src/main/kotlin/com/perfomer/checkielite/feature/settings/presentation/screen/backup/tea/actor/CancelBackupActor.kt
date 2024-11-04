package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.CancelBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class CancelBackupActor(
    private val repository: BackupRepository,
) : Actor<BackupCommand, BackupEvent> {

    override fun act(commands: Flow<BackupCommand>): Flow<BackupEvent> {
        return commands.filterIsInstance<CancelBackup>()
            .mapLatest(::handleCommand)
            .ignoreResult()
    }

    private suspend fun handleCommand(command: CancelBackup) {
        repository.cancelBackup()
    }
}