package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class SearchNavigationActor(
    private val router: Router,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<SearchNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: SearchNavigationCommand): SearchNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
        }

        return null
    }
}