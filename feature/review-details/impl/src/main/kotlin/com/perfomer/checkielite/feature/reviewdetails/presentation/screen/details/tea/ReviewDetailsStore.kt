package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.Initialize
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiStateMapper

internal class ReviewDetailsStore(
    componentContext: ComponentContext,
    destination: ReviewDetailsDestination,
    reducer: ReviewDetailsReducer,
    uiStateMapper: ReviewDetailsUiStateMapper,
    actors: Set<Actor<ReviewDetailsCommand, ReviewDetailsEvent>>,
) : ComponentStore<ReviewDetailsCommand, ReviewDetailsEffect, ReviewDetailsEvent, ReviewDetailsUiEvent, ReviewDetailsState, ReviewDetailsUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = ReviewDetailsState(destination.reviewId),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("ReviewDetailsStore"),
)