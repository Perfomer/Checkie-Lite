package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsDestination
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEffect
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsState
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiState
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiStateMapper

internal class TagsStore(
    componentContext: ComponentContext,
    destination: TagsDestination,
    reducer: TagsReducer,
    uiStateMapper: TagsUiStateMapper,
    actors: Set<Actor<TagsCommand, TagsEvent>>,
) : ComponentStore<TagsCommand, TagsEffect, TagsEvent, TagsUiEvent, TagsState, TagsUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = TagsState(selectedTagsIds = destination.selectedTagsIds),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("TagsStore"),
)