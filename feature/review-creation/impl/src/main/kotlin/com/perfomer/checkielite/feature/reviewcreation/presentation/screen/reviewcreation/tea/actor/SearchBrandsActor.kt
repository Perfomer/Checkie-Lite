package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.search.smartFilterByQuery
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.BrandsSearchComplete
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class SearchBrandsActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<ReviewCreationCommand.SearchBrands>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: ReviewCreationCommand.SearchBrands): Flow<BrandsSearchComplete> {
        return flowBy {
            localDataSource.getAllBrands()
                .smartFilterByQuery(command.query) { listOf(this to 1F) }
                .take(MAX_RESULTS)
        }
            .map(::BrandsSearchComplete)
            .onCatchLog(TAG, "Failed to search brands", rethrow = false)
    }

    private companion object {
        private const val TAG = "SearchBrandsActor"
        private const val MAX_RESULTS = 3
    }
}