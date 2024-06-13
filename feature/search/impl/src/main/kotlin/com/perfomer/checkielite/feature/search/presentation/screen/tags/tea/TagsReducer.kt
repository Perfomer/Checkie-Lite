package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsResult
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEffect
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent.TagsLoading
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsState
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnSearchQueryInput
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent.OnTagClick

internal class TagsReducer : DslReducer<TagsCommand, TagsEffect, TagsEvent, TagsState>() {

    override fun reduce(event: TagsEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is TagsUiEvent -> reduceUi(event)
        is TagsLoading -> reduceTagsLoading(event)
    }

    private fun reduceInitialize() {
        updateSearchQuery()
    }

    private fun reduceUi(event: TagsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> commands(ExitWithResult(TagsResult.Success(state.selectedTagsIds.toArrayList())))
        is OnTagClick -> {
            val isAdding = state.selectedTagsIds.none { it == event.tagId }

            if (isAdding) {
                state { copy(selectedTagsIds = selectedTagsIds + event.tagId) }
            } else {
                state { copy(selectedTagsIds = selectedTagsIds - event.tagId) }
            }
        }

        is OnSearchQueryInput -> updateSearchQuery(event.query)
        is OnSearchQueryClearClick -> updateSearchQuery("")
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> state { copy(suggestedTags = Lce.Loading(state.suggestedTags.content)) }
        is TagsLoading.Succeed -> state { copy(suggestedTags = Lce.Content(event.tags)) }
        is TagsLoading.Failed -> state { copy(suggestedTags = Lce.Error()) }
    }

    private fun updateSearchQuery(searchQuery: String = state.searchQuery) {
        state { copy(searchQuery = searchQuery) }
        commands(LoadTags(searchQuery = searchQuery.trim()))
    }
}