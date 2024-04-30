package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.actor

import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent.TagsLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoadTagsActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<FilterCommand, FilterEvent> {

    override fun act(commands: Flow<FilterCommand>): Flow<FilterEvent> {
        return commands.filterIsInstance<LoadTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTags): Flow<FilterEvent> {
        return localDataSource.getTags()
            .map(TagsLoading::Succeed)
            .startWith(TagsLoading.Started)
            .onCatchReturn(TagsLoading::Failed)
    }
}