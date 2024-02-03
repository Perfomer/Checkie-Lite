package group.bakemate.feature.splash.presentation.tea

import group.bakemate.tea.tea.dsl.DslReducer
import group.bakemate.feature.splash.presentation.tea.core.SplashCommand
import group.bakemate.feature.splash.presentation.tea.core.SplashCommand.GetLoggedInStatus
import group.bakemate.feature.splash.presentation.tea.core.SplashEffect
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent
import group.bakemate.feature.splash.presentation.tea.core.SplashEvent.LoggedInStatusSucceed
import group.bakemate.feature.splash.presentation.tea.core.SplashNavigationCommand.OpenMain
import group.bakemate.feature.splash.presentation.tea.core.SplashNavigationCommand.OpenWelcome
import group.bakemate.feature.splash.presentation.tea.core.SplashState
import group.bakemate.feature.splash.presentation.tea.core.SplashUiEvent.AnimationEnd
import kotlinx.coroutines.delay

internal class SplashReducer : DslReducer<SplashCommand, SplashEffect, SplashEvent, SplashState>() {

    override fun reduce(event: SplashEvent) = when (event) {
        is AnimationEnd -> commands(GetLoggedInStatus)

        is LoggedInStatusSucceed -> if(event.isLoggedIn) commands(OpenMain) else commands(OpenWelcome)
    }
}