package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.WarmUpEmojis
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class WarmUpEmojisActor(
    private val emojiRepository: CheckieEmojiRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<WarmUpEmojis>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: WarmUpEmojis): Flow<ReviewLoading> {
        return flowBy { emojiRepository.warmUp() }
            .map { null }
            .filterNotNull()
    }
}