package group.bakemate.feature.welcome.presentation.tea

import group.bakemate.feature.welcome.presentation.tea.core.WelcomeCommand
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeEffect
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeEvent
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeState
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent
import group.bakemate.tea.tea.component.Actor
import group.bakemate.tea.tea.impl.ScreenModelStore

internal class WelcomeStore(
    reducer: WelcomeReducer,
    actors: Set<Actor<WelcomeCommand, WelcomeEvent>>
) : ScreenModelStore<WelcomeCommand, WelcomeEffect, WelcomeEvent, WelcomeUiEvent, WelcomeState, WelcomeState>(
    initialState = WelcomeState(),
    reducer = reducer,
    actors = actors,
)