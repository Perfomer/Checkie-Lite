package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.entity.backup.BackupProgress

internal sealed interface BackupEvent {

    data object Initialize : BackupEvent

    class BackupProgressUpdated(val progress: BackupProgress) : BackupEvent
}

internal sealed interface BackupUiEvent : BackupEvent {

    data object OnBackPress : BackupUiEvent
}

internal sealed interface BackupNavigationEvent : BackupEvent