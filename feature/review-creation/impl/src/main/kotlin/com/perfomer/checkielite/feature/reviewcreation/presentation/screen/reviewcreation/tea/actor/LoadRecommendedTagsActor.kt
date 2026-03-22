package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadRecommendedTags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.RecommendedTagsLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadRecommendedTagsActor(
    private val tagRepository: TagRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<LoadRecommendedTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadRecommendedTags): Flow<RecommendedTagsLoading> {
        return tagRepository.getRecommendedTags(
            reviewId = command.reviewId,
            selectedTagIds = command.selectedTagIds,
            productBrand = command.productBrand,
            maxCount = MAX_RECOMMENDED_TAGS,
        )
            .map(RecommendedTagsLoading::Succeed)
            .onCatchLog(TAG, "Failed to load recommended tags")
            .onCatchReturn { error -> RecommendedTagsLoading.Failed(error) }
            .startWith(RecommendedTagsLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadRecommendedTagsActor"
        private const val MAX_RECOMMENDED_TAGS = 5
    }
}
