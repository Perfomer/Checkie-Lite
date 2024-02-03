package group.bakemate.feature.splash.presentation.tea.core

internal sealed interface SplashCommand {

    object GetLoggedInStatus : SplashCommand
}

internal sealed interface SplashNavigationCommand : SplashCommand {

    object OpenWelcome : SplashNavigationCommand

    object OpenMain : SplashNavigationCommand
}