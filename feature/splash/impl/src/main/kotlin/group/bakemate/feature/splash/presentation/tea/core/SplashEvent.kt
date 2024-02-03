package group.bakemate.feature.splash.presentation.tea.core

internal sealed interface SplashEvent {

    class LoggedInStatusSucceed(val isLoggedIn: Boolean) : SplashEvent
}

internal sealed interface SplashUiEvent : SplashEvent {

    object AnimationEnd : SplashUiEvent
}