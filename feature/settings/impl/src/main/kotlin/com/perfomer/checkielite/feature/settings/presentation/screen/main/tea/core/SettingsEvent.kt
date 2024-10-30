package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEvent {

    data object Initialize : SettingsEvent

    class SyncingStatusUpdated(val isSyncing: Boolean) : SettingsEvent

    class CheckingHasReviewsStatusUpdated(val hasReviews: Boolean) : SettingsEvent
}

internal sealed interface SettingsUiEvent : SettingsEvent {

    data object OnBackPress : SettingsUiEvent

    data object OnBackupExportClick : SettingsUiEvent

    data object OnBackupImportClick : SettingsUiEvent

    data object OnBackupImportConfirmClick : SettingsUiEvent
}

internal sealed interface SettingsNavigationEvent : SettingsEvent {

    sealed interface BackupFileSelection : SettingsNavigationEvent {
        class Succeed(val path: String) : BackupFileSelection
        data object Canceled : BackupFileSelection
    }
}