package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewRepository
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadReviewActor(
    private val repository: ReviewRepository,
) : Actor<ReviewDetailsCommand, ReviewDetailsEvent> {

    override fun act(commands: Flow<ReviewDetailsCommand>): Flow<ReviewDetailsEvent> {
        return commands.filterIsInstance<LoadReview>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadReview): Flow<ReviewLoading> {
        return flowBy { repository.getReview(command.reviewId) }
            .map(ReviewLoading::Succeed)
    }
}