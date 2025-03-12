package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.core.domain.entity.search.SearchFilters
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiState
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiStateMapper

internal class SearchStore(
    componentContext: ComponentContext,
    destination: SearchDestination,
    reducer: SearchReducer,
    uiStateMapper: SearchUiStateMapper,
    actors: Set<Actor<SearchCommand, SearchEvent>>,
) : ComponentStore<SearchCommand, SearchEffect, SearchEvent, SearchUiEvent, SearchState, SearchUiState>(
    componentContext = componentContext,
    initialState = SearchState(
        searchFilters = SearchFilters(
            tagsIds = listOfNotNull(destination.tagId),
        )
    ),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)