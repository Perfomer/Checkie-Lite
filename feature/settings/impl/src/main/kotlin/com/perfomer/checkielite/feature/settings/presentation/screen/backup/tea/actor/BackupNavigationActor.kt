package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor

import com.perfomer.checkielite.common.android.AppRestarter
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationCommand.RestartApp
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class BackupNavigationActor(
    private val router: Router,
    private val appRestarter: AppRestarter,
) : Actor<BackupCommand, BackupEvent> {

    override fun act(commands: Flow<BackupCommand>): Flow<BackupEvent> {
        return commands.filterIsInstance<BackupNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: BackupNavigationCommand): BackupNavigationEvent? {
        when (command) {
            is Exit -> router.exit()
            is RestartApp -> appRestarter.restart()
        }

        return null
    }
}