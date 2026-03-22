package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.SearchRepository
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.RememberTagSearchSortingStrategy
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

internal class RememberTagSearchSortingStrategyActor(
    private val searchRepository: SearchRepository,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<RememberTagSearchSortingStrategy>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: RememberTagSearchSortingStrategy): Flow<SearchEvent> {
        return flowBy { searchRepository.setLatestTagSearchSortingStrategy(command.strategy) }
            .onCatchLog(TAG, "Failed to remember tag search sorting strategy", rethrow = false)
            .ignoreResult()
    }

    private companion object {
        private const val TAG = "RememberTagSearchSortingStrategyActor"
    }
}
