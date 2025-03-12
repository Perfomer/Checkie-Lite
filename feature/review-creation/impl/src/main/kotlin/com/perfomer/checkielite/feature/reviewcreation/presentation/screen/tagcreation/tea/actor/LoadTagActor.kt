package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.LoadTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadTagActor(
    private val tagRepository: TagRepository,
) : Actor<TagCreationCommand, TagCreationEvent> {

    override fun act(commands: Flow<TagCreationCommand>): Flow<TagCreationEvent> {
        return commands.filterIsInstance<LoadTag>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTag): Flow<TagLoading> {
        return flowBy { tagRepository.getTag(command.id) }
            .map(TagLoading::Succeed)
            .onCatchLog(TAG, "Failed to load tag")
            .onCatchReturn(TagLoading::Failed)
            .startWith(TagLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadTagActor"
    }
}