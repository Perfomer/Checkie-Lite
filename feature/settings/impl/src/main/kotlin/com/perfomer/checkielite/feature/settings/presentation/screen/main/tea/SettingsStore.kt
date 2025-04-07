package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiStateMapper

internal class SettingsStore(
    componentContext: ComponentContext,
    reducer: SettingsReducer,
    uiStateMapper: SettingsUiStateMapper,
    actors: Set<Actor<SettingsCommand, SettingsEvent>>,
) : ComponentStore<SettingsCommand, SettingsEffect, SettingsEvent, SettingsUiEvent, SettingsState, SettingsUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = SettingsState(),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("SettingsStore"),
)