package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.ExternalDestination
import com.perfomer.checkielite.core.navigation.api.ExternalResult
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.SelectBackupFile
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.BackupFileSelection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class SettingsNavigationActor(
    private val router: Router,
    private val externalRouter: ExternalRouter,
) : Actor<SettingsCommand, SettingsEvent> {

    override fun act(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
        return commands.filterIsInstance<SettingsNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: SettingsNavigationCommand): SettingsNavigationEvent? {
        when (command) {
            is Exit -> router.exit()
            is SelectBackupFile -> return selectBackupFile()
        }

        return null
    }

    private suspend fun selectBackupFile(): BackupFileSelection {
        val result = externalRouter.navigateForResult<String?>(ExternalDestination.FILE)

        if (result !is ExternalResult.Success) return BackupFileSelection.Canceled
        val filePath = result.result ?: return BackupFileSelection.Canceled

        return BackupFileSelection.Succeed(filePath)
    }
}