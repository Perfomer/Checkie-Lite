package group.bakemate.feature.registration.presentation.tea

import group.bakemate.feature.registration.presentation.tea.core.RegistrationCommand
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEffect
import group.bakemate.feature.registration.presentation.tea.core.RegistrationEvent
import group.bakemate.feature.registration.presentation.tea.core.RegistrationState
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent
import group.bakemate.feature.registration.presentation.tea.core.RegistrationUiEvent.Initialize
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiState
import group.bakemate.feature.registration.presentation.ui.state.RegistrationUiStateMapper
import group.bakemate.tea.tea.component.Actor
import group.bakemate.tea.tea.impl.ScreenModelStore

internal class RegistrationStore(
    reducer: RegistrationReducer,
    uiStateMapper: RegistrationUiStateMapper,
    actors: Set<Actor<RegistrationCommand, RegistrationEvent>>
) : ScreenModelStore<RegistrationCommand, RegistrationEffect, RegistrationEvent, RegistrationUiEvent, RegistrationState, RegistrationUiState>(
    initialState = RegistrationState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize)
)