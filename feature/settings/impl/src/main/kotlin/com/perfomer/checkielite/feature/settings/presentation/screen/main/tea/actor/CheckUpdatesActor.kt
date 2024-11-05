package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.lce
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckUpdates
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.UpdatesCheck
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckUpdatesActor(
    private val appUpdateManager: AppUpdateManager,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<CheckUpdates>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CheckUpdates): Flow<SettingsEvent> {
        return flowBy { appUpdateManager.checkUpdateAvailability() }
            .onCatchLog(TAG, "Failed to check if has updates")
            .lce()
            .map(::UpdatesCheck)
    }

    private companion object {
        private const val TAG = "CheckUpdatesActor"
    }
}