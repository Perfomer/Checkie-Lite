package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadReviews
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.ReviewsLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadReviewsActor(
    private val reviewRepository: ReviewRepository,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<LoadReviews>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadReviews): Flow<ReviewsLoading> {
        return reviewRepository.getReviews()
            .map(ReviewsLoading::Succeed)
            .startWith(ReviewsLoading.Started)
            .onCatchLog(TAG, "Failed to load reviews")
            .onCatchReturn(ReviewsLoading::Failed)
    }

    private companion object {
        private const val TAG = "LoadReviewsActor"
    }
}