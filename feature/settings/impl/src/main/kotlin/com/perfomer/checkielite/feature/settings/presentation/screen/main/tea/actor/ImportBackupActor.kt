package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ImportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.BackupImporting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class ImportBackupActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<ImportBackup>()
            .flatMapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: ImportBackup): Flow<SettingsEvent> {
        return flowBy { localDataSource.importBackup(command.path) }
            .map { BackupImporting.Succeed }
            .startWith(BackupImporting.Started)
            .onCatchReturn(BackupImporting::Failed)
    }
}