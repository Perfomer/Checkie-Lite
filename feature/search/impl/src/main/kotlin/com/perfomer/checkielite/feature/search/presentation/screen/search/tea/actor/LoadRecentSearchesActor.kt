package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.SearchRepository
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.RecentSearchesLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadRecentSearchesActor(
    private val searchRepository: SearchRepository,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<LoadRecentSearches>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadRecentSearches): Flow<RecentSearchesLoading> {
        return searchRepository.getRecentSearches()
            .map(RecentSearchesLoading::Succeed)
            .startWith(RecentSearchesLoading.Started)
            .onCatchLog(TAG, "Failed to load recent searches")
            .onCatchReturn(RecentSearchesLoading::Failed)
    }

    private companion object {
        private const val TAG = "LoadRecentSearchesActor"
    }
}