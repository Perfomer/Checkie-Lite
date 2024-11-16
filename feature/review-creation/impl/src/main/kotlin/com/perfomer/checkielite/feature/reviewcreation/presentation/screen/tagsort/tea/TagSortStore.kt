package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state.TagSortUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state.TagSortUiStateMapper

internal class TagSortStore(
    componentContext: ComponentContext,
    destination: TagSortDestination,
    reducer: TagSortReducer,
    uiStateMapper: TagSortUiStateMapper,
    actors: Set<Actor<TagSortCommand, TagSortEvent>>,
) : ComponentStore<TagSortCommand, TagSortEffect, TagSortEvent, TagSortUiEvent, TagSortState, TagSortUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = TagSortState(
        currentOption = destination.currentOption,
    ),
)