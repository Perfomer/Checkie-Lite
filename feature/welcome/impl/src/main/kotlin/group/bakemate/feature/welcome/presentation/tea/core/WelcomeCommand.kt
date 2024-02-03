package group.bakemate.feature.welcome.presentation.tea.core

internal sealed interface WelcomeCommand

internal sealed interface WelcomeNavigationCommand : WelcomeCommand {

    object OpenLogin : WelcomeNavigationCommand

    object OpenRegistration : WelcomeNavigationCommand
}