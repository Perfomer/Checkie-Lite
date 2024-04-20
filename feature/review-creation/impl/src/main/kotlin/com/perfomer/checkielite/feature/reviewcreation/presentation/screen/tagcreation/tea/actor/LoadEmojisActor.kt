package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor

import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.LoadEmojis
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.EmojisLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadEmojisActor(
    private val emojiRepository: CheckieEmojiRepository,
) : Actor<TagCreationCommand, TagCreationEvent> {

    override fun act(commands: Flow<TagCreationCommand>): Flow<TagCreationEvent> {
        return commands.filterIsInstance<LoadEmojis>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadEmojis): Flow<EmojisLoading> {
        return flowBy { emojiRepository.getCategorizedEmojis() }
            .map(EmojisLoading::Succeed)
            .onCatchReturn(EmojisLoading::Failed)
            .startWith(EmojisLoading.Started)
    }
}