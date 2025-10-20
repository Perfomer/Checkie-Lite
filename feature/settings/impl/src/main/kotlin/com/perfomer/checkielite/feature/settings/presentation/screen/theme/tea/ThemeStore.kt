package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.settings.presentation.navigation.ThemeDestination
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeState
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state.ThemeUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state.ThemeUiStateMapper

internal class ThemeStore(
    componentContext: ComponentContext,
    destination: ThemeDestination,
    reducer: ThemeReducer,
    uiStateMapper: ThemeUiStateMapper,
    actors: Set<Actor<ThemeCommand, ThemeEvent>>,
) : ComponentStore<ThemeCommand, ThemeEffect, ThemeEvent, ThemeUiEvent, ThemeState, ThemeUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = ThemeState(
        currentTheme = destination.currentTheme,
    ),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("ThemeStore"),
)