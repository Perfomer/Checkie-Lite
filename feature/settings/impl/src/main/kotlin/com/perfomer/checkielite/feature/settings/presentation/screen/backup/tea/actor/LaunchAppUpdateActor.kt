package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.common.update.api.updateIfAvailable
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.LaunchAppUpdate
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

internal class LaunchAppUpdateActor(
    private val appUpdateManager: AppUpdateManager,
) : Actor<BackupCommand, BackupEvent> {

    override fun act(commands: Flow<BackupCommand>): Flow<BackupEvent> {
        return commands.filterIsInstance<LaunchAppUpdate>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LaunchAppUpdate): Flow<BackupEvent> {
        return flowBy { appUpdateManager.updateIfAvailable() }
            .onCatchLog(TAG, "Failed to update app", rethrow = false)
            .ignoreResult()
    }

    private companion object {
        private const val TAG = "LaunchAppUpdateActor"
    }
}