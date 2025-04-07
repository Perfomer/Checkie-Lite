package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ComponentStore
import com.perfomer.checkielite.common.ui.util.tea.LogUnhandledExceptionHandler
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupDestination
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiStateMapper

internal class BackupStore(
    componentContext: ComponentContext,
    destination: BackupDestination,
    reducer: BackupReducer,
    uiStateMapper: BackupUiStateMapper,
    actors: Set<Actor<BackupCommand, BackupEvent>>,
) : ComponentStore<BackupCommand, BackupEffect, BackupEvent, BackupUiEvent, BackupState, BackupUiState>(
    componentContext = componentContext,
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialState = BackupState(mode = destination.mode),
    initialEvents = listOf(Initialize),
    unhandledExceptionHandler = LogUnhandledExceptionHandler("BackupStore"),
)