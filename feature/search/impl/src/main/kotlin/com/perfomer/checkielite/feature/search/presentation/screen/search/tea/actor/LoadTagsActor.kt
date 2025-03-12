package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.TagsLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadTagsActor(
    private val tagRepository: TagRepository,
) : Actor<SearchCommand, SearchEvent> {

    override fun act(commands: Flow<SearchCommand>): Flow<SearchEvent> {
        return commands.filterIsInstance<LoadTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTags): Flow<SearchEvent> {
        return tagRepository.getTags()
            .map(TagsLoading::Succeed)
            .startWith(TagsLoading.Started)
            .onCatchLog(TAG, "Failed to load tags")
            .onCatchReturn(TagsLoading::Failed)
    }

    private companion object {
        private const val TAG = "LoadTagsActor"
    }
}