package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.common.update.api.updateIfAvailable
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LaunchAppUpdate
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class LaunchAppUpdateActor(
    private val appUpdateManager: AppUpdateManager,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<LaunchAppUpdate>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LaunchAppUpdate): Flow<SettingsEvent> {
        return flowBy { appUpdateManager.updateIfAvailable() }
            .onCatchLog(TAG, "Failed to check if has updates", rethrow = false)
            .ignoreResult()
    }

    private companion object {
        private const val TAG = "LaunchAppUpdateActor"
    }
}