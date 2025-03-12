package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.BrandRepository
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadReviewActor(
    private val reviewRepository: ReviewRepository,
    private val brandRepository: BrandRepository,
) : Actor<ReviewDetailsCommand, ReviewDetailsEvent> {

    override fun act(commands: Flow<ReviewDetailsCommand>): Flow<ReviewDetailsEvent> {
        return commands.filterIsInstance<LoadReview>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadReview): Flow<ReviewLoading> {
        return combine(
            reviewRepository.getReview(command.reviewId),
            brandRepository.getRecommendations(command.reviewId),
            ::ReviewDetails
        )
            .map(ReviewLoading::Succeed)
            .onCatchLog(TAG, "Failed to load review")
            .onCatchReturn(ReviewLoading::Failed)
            .startWith(ReviewLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadReviewActor"
    }
}