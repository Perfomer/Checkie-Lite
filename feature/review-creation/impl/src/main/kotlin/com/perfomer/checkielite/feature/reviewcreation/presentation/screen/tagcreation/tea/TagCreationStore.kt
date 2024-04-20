package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiStateMapper

internal class TagCreationStore(
    params: TagCreationParams,
    reducer: TagCreationReducer,
    uiStateMapper: TagCreationUiStateMapper,
    actors: Set<Actor<TagCreationCommand, TagCreationEvent>>,
) : ScreenModelStore<TagCreationCommand, TagCreationEffect, TagCreationEvent, TagCreationUiEvent, TagCreationState, TagCreationUiState>(
    initialState = TagCreationState(mode = params.mode),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)