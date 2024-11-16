package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class SortNavigationActor(
    private val router: Router,
) : Actor<SortCommand, SortEvent> {

    override fun act(commands: Flow<SortCommand>): Flow<SortEvent> {
        return commands.filterIsInstance<SortNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: SortNavigationCommand): SortNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
            is ExitWithResult -> exitWithResult(command.result)
        }

        return null
    }
}