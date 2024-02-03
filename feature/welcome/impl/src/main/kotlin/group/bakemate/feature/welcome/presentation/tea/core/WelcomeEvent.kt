package group.bakemate.feature.welcome.presentation.tea.core

internal sealed interface WelcomeEvent

internal sealed interface WelcomeUiEvent : WelcomeEvent {

    object OnLoginButtonClick : WelcomeUiEvent

    object OnRegisterButtonClick : WelcomeUiEvent
}