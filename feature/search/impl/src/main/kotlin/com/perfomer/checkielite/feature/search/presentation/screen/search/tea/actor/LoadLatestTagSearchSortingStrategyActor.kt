package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.SearchRepository
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadLatestTagSearchSortingStrategy
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.LatestTagSearchSortingStrategyStatusUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadLatestTagSearchSortingStrategyActor(
    private val searchRepository: SearchRepository,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<LoadLatestTagSearchSortingStrategy>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadLatestTagSearchSortingStrategy): Flow<SearchEvent> {
        return flowBy { searchRepository.getLatestTagSearchSortingStrategy() }
            .map { sorting -> LatestTagSearchSortingStrategyStatusUpdated(Lce.Content(sorting)) }
            .onCatchLog(TAG, "Failed to load latest tag search sorting strategy")
            .onCatchReturn { error -> LatestTagSearchSortingStrategyStatusUpdated(Lce.Error(error)) }
            .startWith(LatestTagSearchSortingStrategyStatusUpdated(Lce.Loading()))
    }

    private companion object {
        private const val TAG = "LoadLatestTagSearchSortingStrategyActor"
    }
}
