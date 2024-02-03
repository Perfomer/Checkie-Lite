package group.bakemate.feature.registration.presentation.ui.state

import group.bakemate.feature.registration.presentation.tea.core.RegistrationState
import group.bakemate.feature.registration.presentation.ui.factory.RegistrationFactory
import group.bakemate.tea.tea.component.UiStateMapper

internal class RegistrationUiStateMapper(
    private val registrationFactory: RegistrationFactory
) : UiStateMapper<RegistrationState, RegistrationUiState> {

    override fun map(state: RegistrationState): RegistrationUiState {
        return RegistrationUiState(
            textFields = registrationFactory.create(state),
            isInputEnabled = !state.isSending
        )
    }
}