package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CreateReviewActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<CreateReview>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CreateReview): Flow<ReviewSaving> {
        return flowBy {
            localDataSource.createReview(
                productName = command.productName,
                productBrand = command.productBrand,
                price = command.price,
                rating = command.rating,
                pictures = command.pictures,
                tagsIds = command.tagsIds,
                comment = command.comment,
                advantages = command.advantages,
                disadvantages = command.disadvantages,
            )
        }
            .map { ReviewSaving.Succeed }
            .onCatchLog(TAG, "Failed to create review")
            .onCatchReturn { ReviewSaving.Failed }
            .startWith(ReviewSaving.Started)
    }

    private companion object {
        private const val TAG = "CreateReviewActor"
    }
}