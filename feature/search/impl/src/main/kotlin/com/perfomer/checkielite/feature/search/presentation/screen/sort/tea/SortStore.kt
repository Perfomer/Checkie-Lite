package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.search.presentation.navigation.SortDestination
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortCommand
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEffect
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiState
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiStateMapper

internal class SortStore(
    componentContext: ComponentContext,
    destination: SortDestination,
    reducer: SortReducer,
    uiStateMapper: SortUiStateMapper,
    actors: Set<Actor<SortCommand, SortEvent>>,
) : ComponentStore<SortCommand, SortEffect, SortEvent, SortUiEvent, SortState, SortUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = SortState(
        currentOption = destination.sorting,
    ),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("SortStore"),
)