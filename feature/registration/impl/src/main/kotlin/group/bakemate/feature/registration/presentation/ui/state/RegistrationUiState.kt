package group.bakemate.feature.registration.presentation.ui.state

internal data class RegistrationUiState(
    val textFields: List<TextField>,
    val isInputEnabled: Boolean,
)