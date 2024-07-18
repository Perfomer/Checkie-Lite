package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupEvent {

    data object Initialize : BackupEvent

    sealed interface BackupExporting : BackupEvent {
        data object Started : BackupExporting
        data object Succeed : BackupExporting
        class Failed(val error: Throwable) : BackupExporting
    }

    sealed interface BackupImporting : BackupEvent {
        data object Started : BackupImporting
        data object Succeed : BackupImporting
        class Failed(val error: Throwable) : BackupImporting
    }
}

internal sealed interface BackupUiEvent : BackupEvent {

    data object OnBackPress : BackupUiEvent
}

internal sealed interface BackupNavigationEvent : BackupEvent