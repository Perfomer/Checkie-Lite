package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiStateMapper

internal class ReviewCreationStore(
    componentContext: ComponentContext,
    destination: ReviewCreationDestination,
    reducer: ReviewCreationReducer,
    uiStateMapper: ReviewCreationUiStateMapper,
    actors: Set<Actor<ReviewCreationCommand, ReviewCreationEvent>>,
) : ComponentStore<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationUiEvent, ReviewCreationState, ReviewCreationUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = ReviewCreationState(mode = destination.mode),
    initialEvents = listOf(Initialize),
)