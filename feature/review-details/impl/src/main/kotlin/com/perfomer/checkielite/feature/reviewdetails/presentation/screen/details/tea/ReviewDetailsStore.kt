package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiStateMapper

internal class ReviewDetailsStore(
    params: ReviewDetailsParams,
    reducer: ReviewDetailsReducer,
    uiStateMapper: ReviewDetailsUiStateMapper,
    actors: Set<Actor<ReviewDetailsCommand, ReviewDetailsEvent>>,
) : ScreenModelStore<ReviewDetailsCommand, ReviewDetailsEffect, ReviewDetailsEvent, ReviewDetailsUiEvent, ReviewDetailsState, ReviewDetailsUiState>(
    initialState = ReviewDetailsState(params.reviewId),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)