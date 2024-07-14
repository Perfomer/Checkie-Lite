package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEvent {

    data object Initialize : SettingsEvent

    sealed interface BackupExporting : SettingsEvent {
        data object Started : BackupExporting
        data object Succeed : BackupExporting
        class Failed(val error: Throwable) : BackupExporting
    }

    sealed interface BackupImporting : SettingsEvent {
        data object Started : BackupImporting
        data object Succeed : BackupImporting
        class Failed(val error: Throwable) : BackupImporting
    }
}

internal sealed interface SettingsUiEvent : SettingsEvent {

    data object OnBackPress : SettingsUiEvent

    data object OnBackupExportClick : SettingsUiEvent

    data object OnBackupImportClick : SettingsUiEvent
}

internal sealed interface SettingsNavigationEvent : SettingsEvent {

    sealed interface BackupFileSelection : SettingsNavigationEvent {
        class Succeed(val path: String) : BackupFileSelection
        data object Canceled : BackupFileSelection
    }
}