package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.RememberTagSortStrategy
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

internal class RememberTagSortStrategyActor(
    private val tagRepository: TagRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<RememberTagSortStrategy>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: RememberTagSortStrategy): Flow<ReviewCreationEvent> {
        return flowBy { tagRepository.setLatestTagSortingStrategy(command.strategy) }
            .onCatchLog(TAG, "Failed to remember tag sort strategy", rethrow = false)
            .ignoreResult()
    }

    private companion object {
        private const val TAG = "RememberTagSortStrategyActor"
    }
}