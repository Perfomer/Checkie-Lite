package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterParams
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterCommand
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEffect
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterState
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.state.FilterUiState
import com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.state.FilterUiStateMapper

internal class FilterStore(
    params: FilterParams,
    reducer: FilterReducer,
    uiStateMapper: FilterUiStateMapper,
    actors: Set<Actor<FilterCommand, FilterEvent>>,
) : ScreenModelStore<FilterCommand, FilterEffect, FilterEvent, FilterUiEvent, FilterState, FilterUiState>(
    initialState = FilterState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)