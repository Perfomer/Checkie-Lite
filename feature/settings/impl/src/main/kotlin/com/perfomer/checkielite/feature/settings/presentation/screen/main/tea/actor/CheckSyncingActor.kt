package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.AppRepository
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckSyncing
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.SyncingStatusUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CheckSyncingActor(
    private val appRepository: AppRepository,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<CheckSyncing>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CheckSyncing): Flow<SettingsEvent> {
        return appRepository.isSyncing()
            .map(::SyncingStatusUpdated)
            .onCatchLog(TAG, "Failed to check syncing status", rethrow = false)
    }

    private companion object {
        private const val TAG = "CheckSyncingActor"
    }
}