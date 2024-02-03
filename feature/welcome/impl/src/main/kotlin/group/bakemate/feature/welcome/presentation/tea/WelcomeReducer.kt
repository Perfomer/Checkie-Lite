package group.bakemate.feature.welcome.presentation.tea

import group.bakemate.feature.welcome.presentation.tea.core.WelcomeCommand
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeEffect
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeEvent
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeNavigationCommand.OpenLogin
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeNavigationCommand.OpenRegistration
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeState
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent.OnLoginButtonClick
import group.bakemate.feature.welcome.presentation.tea.core.WelcomeUiEvent.OnRegisterButtonClick
import group.bakemate.tea.tea.dsl.DslReducer

internal class WelcomeReducer : DslReducer<WelcomeCommand, WelcomeEffect, WelcomeEvent, WelcomeState>() {

    override fun reduce(event: WelcomeEvent): Any = when(event) {
        is WelcomeUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: WelcomeUiEvent) = when(event) {
        is OnLoginButtonClick -> commands(OpenLogin)
        is OnRegisterButtonClick -> commands(OpenRegistration)
    }
}