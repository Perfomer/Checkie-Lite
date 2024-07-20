package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

internal sealed interface BackupEvent {

    data object Initialize : BackupEvent

    sealed interface Backup : BackupEvent {
        class ProgressUpdated(val progress: Float) : Backup
        data object Completed : Backup
    }

    data object AwaitCompleted : BackupEvent
}

internal sealed interface BackupUiEvent : BackupEvent {

    data object OnBackPress : BackupUiEvent
}

internal sealed interface BackupNavigationEvent : BackupEvent