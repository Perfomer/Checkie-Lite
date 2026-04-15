package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogNavigationCommand
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogNavigationCommand.Exit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class ChangelogNavigationActor(
    private val router: Router,
) : Actor<ChangelogCommand, ChangelogEvent> {

    override fun act(commands: Flow<ChangelogCommand>): Flow<ChangelogEvent> {
        return commands.filterIsInstance<ChangelogNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: ChangelogNavigationCommand): ChangelogEvent? {
        when (command) {
            is Exit -> router.exit()
        }

        return null
    }
}
