package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.Initialize
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiStateMapper
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore

internal class MainStore(
    reducer: MainReducer,
    uiStateMapper: MainUiStateMapper,
    actors: Set<Actor<MainCommand, MainEvent>>,
) : ScreenModelStore<MainCommand, MainEffect, MainEvent, MainUiEvent, MainState, MainUiState>(
    initialState = MainState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)