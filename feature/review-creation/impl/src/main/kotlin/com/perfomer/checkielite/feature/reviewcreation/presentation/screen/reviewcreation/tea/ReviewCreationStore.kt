package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiStateMapper

internal class ReviewCreationStore(
    reducer: ReviewCreationReducer,
    uiStateMapper: ReviewCreationUiStateMapper,
    actors: Set<Actor<ReviewCreationCommand, ReviewCreationEvent>>,
) : ScreenModelStore<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationUiEvent, ReviewCreationState, ReviewCreationUiState>(
    initialState = ReviewCreationState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)