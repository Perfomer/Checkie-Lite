package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.CreateTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagSaving
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CreateTagActor(
    private val tagRepository: TagRepository,
) : Actor<TagCreationCommand, TagCreationEvent> {

    override fun act(commands: Flow<TagCreationCommand>): Flow<TagCreationEvent> {
        return commands.filterIsInstance<CreateTag>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: CreateTag): Flow<TagSaving> {
        return flowBy { tagRepository.createTag(value = command.value, emoji = command.emoji) }
            .map(TagSaving::Succeed)
            .onCatchLog(TAG, "Failed to create tag")
            .onCatchReturn(TagSaving::Failed)
            .startWith(TagSaving.Started)
    }

    private companion object {
        private const val TAG = "CreateTagActor"
    }
}