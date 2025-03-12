package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadLatestTagSortStrategy
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.LatestTagSortStrategyLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadLatestTagSortStrategyActor(
    private val tagRepository: TagRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<LoadLatestTagSortStrategy>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadLatestTagSortStrategy): Flow<ReviewCreationEvent> {
        return flowBy { tagRepository.getLatestTagSortingStrategy() }
            .map(LatestTagSortStrategyLoading::Succeed)
            .onCatchLog(TAG, "Failed to load latest tag sort strategy")
            .onCatchReturn(LatestTagSortStrategyLoading::Failed)
            .startWith(LatestTagSortStrategyLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadLatestTagSortStrategyActor"
    }
}