package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEffect
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiStateMapper

internal class SortStore(
    params: SortParams,
    reducer: SortReducer,
    uiStateMapper: SortUiStateMapper,
    actors: Set<Actor<SortCommand, SortEvent>>,
) : ScreenModelStore<SortCommand, SortEffect, SortEvent, SortUiEvent, SortState, SortUiState>(
    initialState = SortState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
)