package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.Initialize
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiStateMapper

internal class MainStore(
    componentContext: ComponentContext,
    reducer: MainReducer,
    uiStateMapper: MainUiStateMapper,
    actors: Set<Actor<MainCommand, MainEvent>>,
) : ComponentStore<MainCommand, MainEffect, MainEvent, MainUiEvent, MainState, MainUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = MainState(),
    initialEvents = listOf(Initialize),
)