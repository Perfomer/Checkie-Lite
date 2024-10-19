package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadReviews
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.ReviewsLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadReviewsActor(
    private val repository: ReviewsRepository,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<LoadReviews>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadReviews): Flow<ReviewsLoading> {
        return repository.getCheckies()
            .map(ReviewsLoading::Succeed)
            .startWith(ReviewsLoading.Started)
            .onCatchLog(TAG, "Failed to load reviews")
            .onCatchReturn(ReviewsLoading::Failed)
    }

    private companion object {
        private const val TAG = "LoadReviewsActor"
    }
}