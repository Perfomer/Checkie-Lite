package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.RememberRecentSearch
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

internal class RememberRecentSearchActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<RememberRecentSearch>()
            .mapLatest(::handleCommand)
            .onCatchLog(TAG, "Failed to remember recent search")
            .ignoreResult()
    }

    private suspend fun handleCommand(command: RememberRecentSearch) {
        localDataSource.rememberRecentSearch(command.reviewId)
    }

    private companion object {
        private const val TAG = "RememberRecentSearchActor"
    }
}