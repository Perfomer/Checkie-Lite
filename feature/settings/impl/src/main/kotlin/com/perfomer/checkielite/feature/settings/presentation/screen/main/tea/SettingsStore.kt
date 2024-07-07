package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiStateMapper

internal class SettingsStore(
    reducer: SettingsReducer,
    uiStateMapper: SettingsUiStateMapper,
    actors: Set<Actor<SettingsCommand, SettingsEvent>>,
) : ScreenModelStore<SettingsCommand, SettingsEffect, SettingsEvent, SettingsUiEvent, SettingsState, SettingsUiState>(
    initialState = SettingsState(),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)