package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.WarmUpEmojis
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class WarmUpEmojisActor(
    private val emojiRepository: CheckieEmojiRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<WarmUpEmojis>()
            .mapLatest(::handleCommand)
            .ignoreResult()
    }

    private suspend fun handleCommand(command: WarmUpEmojis) {
        emojiRepository.warmUp()
    }
}