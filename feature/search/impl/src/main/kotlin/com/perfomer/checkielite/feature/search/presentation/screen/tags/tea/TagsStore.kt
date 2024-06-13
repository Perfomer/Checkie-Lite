package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsParams
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEffect
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsState
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiState
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiStateMapper

internal class TagsStore(
    params: TagsParams,
    reducer: TagsReducer,
    uiStateMapper: TagsUiStateMapper,
    actors: Set<Actor<TagsCommand, TagsEvent>>,
) : ScreenModelStore<TagsCommand, TagsEffect, TagsEvent, TagsUiEvent, TagsState, TagsUiState>(
    initialState = TagsState(selectedTagsIds = params.selectedTagsIds),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)