package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.entity.BackupProgress
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupProgressUpdated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class ObserveBackupProgressActor(
    private val backupRepository: BackupRepository,
) : Actor<BackupCommand, BackupEvent> {

    override fun act(commands: Flow<BackupCommand>): Flow<BackupEvent> {
        return commands.filterIsInstance<ObserveBackupProgress>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ObserveBackupProgress): Flow<BackupEvent> {
        return backupRepository.observeBackupState()
            .map { state -> state.progress }
            .filter { progress -> progress !is BackupProgress.None }
            .map(::BackupProgressUpdated)
    }
}