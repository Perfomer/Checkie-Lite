package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CreateReviewActor(
    private val repository: ReviewCreationRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<CreateReview>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CreateReview): Flow<ReviewSaving> {
        return flowBy {
            repository.createReview(
                productName = command.productName,
                productBrand = command.productBrand,
                rating = command.rating,
                pictures = command.pictures,
                comment = command.comment,
                advantages = command.advantages,
                disadvantages = command.disadvantages,
            )
        }
            .map { ReviewSaving.Succeed }
            .onCatchReturn { ReviewSaving.Failed }
            .startWith(ReviewSaving.Started)
    }
}