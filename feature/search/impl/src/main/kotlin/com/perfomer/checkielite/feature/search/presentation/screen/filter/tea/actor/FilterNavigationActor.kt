package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationCommand.OpenTags
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterNavigationEvent.OnTagsSelected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class FilterNavigationActor(
    private val router: Router,
) : Actor<FilterCommand, FilterEvent> {

    override fun act(commands: Flow<FilterCommand>): Flow<FilterEvent> {
        return commands.filterIsInstance<FilterNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private suspend fun handleCommand(command: FilterNavigationCommand): FilterNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
            is ExitWithResult -> exitWithResult(command.result)
            is OpenTags -> return openTags()
        }

        return null
    }

    private suspend fun openTags(): OnTagsSelected {
        TODO()
    }
}