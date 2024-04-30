package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.SearchReviews
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.Searching
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchReviewsActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<SearchReviews>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: SearchReviews): Flow<Searching> {
        return localDataSource.findReviews(
            searchQuery = command.query,
            filters = command.filters,
            sorting = command.sorting,
        )
            .map { reviews -> Searching.Succeed(reviews) }
            .startWith(Searching.Started)
            .onCatchReturn(Searching::Failed)
    }
}