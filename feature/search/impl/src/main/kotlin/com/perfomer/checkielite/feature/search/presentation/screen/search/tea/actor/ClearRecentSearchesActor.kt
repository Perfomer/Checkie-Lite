package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.ClearRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class ClearRecentSearchesActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<ClearRecentSearches>()
            .mapLatest(::handleCommand)
            .ignoreResult()
    }

    private suspend fun handleCommand(command: ClearRecentSearches) {
        localDataSource.clearRecentSearches()
    }
}