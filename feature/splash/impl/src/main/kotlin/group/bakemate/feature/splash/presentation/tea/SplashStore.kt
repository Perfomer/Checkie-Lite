package group.bakemate.feature.splash.presentation.tea

import group.bakemate.tea.tea.component.Actor
import group.bakemate.tea.tea.impl.ScreenModelStore
import group.bakemate.feature.splash.presentation.tea.core.SplashCommand
import group.bakemate.feature.splash.presentation.tea.core.SplashEffect
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent
import group.bakemate.feature.splash.presentation.tea.core.SplashState
import group.bakemate.feature.splash.presentation.tea.core.SplashUiEvent

internal class SplashStore(
    reducer: SplashReducer,
    actors: Set<Actor<SplashCommand, SplashEvent>>
) : ScreenModelStore<SplashCommand, SplashEffect, SplashEvent, SplashUiEvent, SplashState, SplashState>(
    initialState = SplashState(),
    reducer = reducer,
    actors = actors,
)