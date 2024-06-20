package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state.TagSortUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state.TagSortUiStateMapper

internal class TagSortStore(
    params: TagSortParams,
    reducer: TagSortReducer,
    uiStateMapper: TagSortUiStateMapper,
    actors: Set<Actor<TagSortCommand, TagSortEvent>>,
) : ScreenModelStore<TagSortCommand, TagSortEffect, TagSortEvent, TagSortUiEvent, TagSortState, TagSortUiState>(
    initialState = TagSortState(
        currentOption = params.currentOption,
    ),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)