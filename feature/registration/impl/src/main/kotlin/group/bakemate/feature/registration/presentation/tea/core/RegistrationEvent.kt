package group.bakemate.feature.registration.presentation.tea.core

import group.bakemate.feature.registration.presentation.entity.FieldType

internal sealed interface RegistrationEvent {

    object RegistrationSucceed : RegistrationEvent

    class RegistrationError(val error: Throwable) : RegistrationEvent
}

internal sealed interface RegistrationUiEvent : RegistrationEvent {

    object Initialize : RegistrationUiEvent

    class OnTextChange(val text: String, val type: FieldType) : RegistrationUiEvent

    object OnBackPressed : RegistrationUiEvent

    object OnButtonClick : RegistrationUiEvent
}