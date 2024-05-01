package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.actor

import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent.TagsLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadTagsActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<TagsCommand, TagsEvent> {

    override fun act(commands: Flow<TagsCommand>): Flow<TagsEvent> {
        return commands.filterIsInstance<LoadTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTags): Flow<TagsEvent> {
        return localDataSource.getTags(searchQuery = command.searchQuery)
            .map(TagsLoading::Succeed)
            .startWith(TagsLoading.Started)
            .onCatchReturn(TagsLoading::Failed)
    }
}