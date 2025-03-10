package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.search.smartFilterByQuery
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadTags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.TagsLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadTagsActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<LoadTags>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadTags): Flow<TagsLoading> {
        return localDataSource.getTags()
            .map { tags ->
                tags.smartFilterByQuery(command.searchQuery) { listOf(value to 1F) }
            }
            .map { tags ->
                if (command.searchQuery.isEmpty()) {
                    when (command.sort) {
                        TagSortingStrategy.USAGE_COUNT -> tags
                        TagSortingStrategy.ALPHABETICALLY -> tags.sortedBy { it.value }
                    }
                } else {
                    tags
                }
            }
            .map(TagsLoading::Succeed)
            .onCatchLog(TAG, "Failed to load tags")
            .onCatchReturn(TagsLoading::Failed)
            .startWith(TagsLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadTagsActor"
    }
}