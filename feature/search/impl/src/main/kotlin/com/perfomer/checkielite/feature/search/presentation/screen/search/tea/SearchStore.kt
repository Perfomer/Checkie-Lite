package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.search.navigation.SearchParams
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.OnInitialize
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiStateMapper

internal class SearchStore(
    params: SearchParams,
    reducer: SearchReducer,
    uiStateMapper: SearchUiStateMapper,
    actors: Set<Actor<SearchCommand, SearchEvent>>,
) : ScreenModelStore<SearchCommand, SearchEffect, SearchEvent, SearchUiEvent, SearchState, SearchUiState>(
    initialState = SearchState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(OnInitialize),
)