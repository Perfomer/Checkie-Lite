package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ObserveBackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Backup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

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
            .mapNotNull { progress ->
                when (progress) {
                    is BackupProgress.None -> null
                    is BackupProgress.InProgress -> Backup.ProgressUpdated(progress.progress)
                    is BackupProgress.Completed -> Backup.Completed
                    is BackupProgress.Failure -> Backup.Failed(progress.error)
                }
            }
    }
}