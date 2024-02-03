package group.bakemate.feature.registration.presentation.tea.core

import group.bakemate.core.entity.RegisterData

internal sealed interface RegistrationCommand {

    class RegisterUser(val registerData: RegisterData) : RegistrationCommand
}

internal sealed interface RegistrationNavigationCommand : RegistrationCommand {

    object Exit : RegistrationNavigationCommand
}