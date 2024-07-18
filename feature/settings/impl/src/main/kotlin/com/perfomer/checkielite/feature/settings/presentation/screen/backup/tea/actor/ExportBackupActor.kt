package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.ExportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.BackupExporting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class ExportBackupActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<BackupCommand, BackupEvent> {

    override fun act(commands: Flow<BackupCommand>): Flow<BackupEvent> {
        return commands.filterIsInstance<ExportBackup>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ExportBackup): Flow<BackupEvent> {
        return flowBy { localDataSource.exportBackup() }
            .map { BackupExporting.Succeed }
            .startWith(BackupExporting.Started)
            .onCatchReturn(BackupExporting::Failed)
    }
}