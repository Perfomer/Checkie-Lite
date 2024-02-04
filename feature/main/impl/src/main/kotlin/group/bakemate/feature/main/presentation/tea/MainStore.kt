package group.bakemate.feature.main.presentation.tea

import group.bakemate.feature.main.presentation.tea.core.MainCommand
import group.bakemate.feature.main.presentation.tea.core.MainEffect
import group.bakemate.feature.main.presentation.tea.core.MainEvent
import group.bakemate.feature.main.presentation.tea.core.MainState
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent
import group.bakemate.feature.main.presentation.tea.core.MainEvent.Initialize
import group.bakemate.feature.main.presentation.ui.state.MainUiState
import group.bakemate.feature.main.presentation.ui.state.MainUiStateMapper
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