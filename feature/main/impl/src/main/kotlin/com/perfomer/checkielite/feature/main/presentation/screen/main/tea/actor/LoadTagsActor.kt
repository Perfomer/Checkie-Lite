package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadTags
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.TagsLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadTagsActor(
    private val tagRepository: TagRepository,
) : Actor<MainCommand, MainEvent> {

    override fun act(commands: Flow<MainCommand>): Flow<MainEvent> {
        return commands.filterIsInstance<LoadTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTags): Flow<MainEvent> {
        return tagRepository.getTags()
            .map(TagsLoading::Succeed)
            .onCatchLog(TAG, "Failed to load tags")
            .onCatchReturn(TagsLoading::Failed)
            .startWith(TagsLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadTagsActor"
    }
}