package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiStateMapper

internal class BackupStore(
    params: BackupParams,
    reducer: BackupReducer,
    uiStateMapper: BackupUiStateMapper,
    actors: Set<Actor<BackupCommand, BackupEvent>>,
) : ScreenModelStore<BackupCommand, BackupEffect, BackupEvent, BackupUiEvent, BackupState, BackupUiState>(
    initialState = BackupState(mode = params.mode),
    reducer = reducer,
    uiStateMapper = uiStateMapper,
    actors = actors,
    initialEvents = listOf(Initialize),
)