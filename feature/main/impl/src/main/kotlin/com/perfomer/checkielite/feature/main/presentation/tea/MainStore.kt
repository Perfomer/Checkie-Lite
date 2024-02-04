package com.perfomer.checkielite.feature.main.presentation.tea

import com.perfomer.checkielite.feature.main.presentation.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainEvent.Initialize
import com.perfomer.checkielite.feature.main.presentation.ui.state.MainUiState
import com.perfomer.checkielite.feature.main.presentation.ui.state.MainUiStateMapper
import group.bakemate.tea.tea.component.Actor
import group.bakemate.tea.tea.impl.ScreenModelStore

internal class MainStore(
    reducer: MainReducer,
    uiStateMapper: MainUiStateMapper,
    actors: Set<Actor<MainCommand, MainEvent>>
) : ScreenModelStore<MainCommand, MainEffect, MainEvent, MainUiEvent, MainState, MainUiState>(
    initialState = MainState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)