package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiStateMapper

internal class TagCreationStore(
    componentContext: ComponentContext,
    destination: TagCreationDestination,
    reducer: TagCreationReducer,
    uiStateMapper: TagCreationUiStateMapper,
    actors: Set<Actor<TagCreationCommand, TagCreationEvent>>,
) : ComponentStore<TagCreationCommand, TagCreationEffect, TagCreationEvent, TagCreationUiEvent, TagCreationState, TagCreationUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = TagCreationState(mode = destination.mode),
    initialEvents = listOf(Initialize),
)