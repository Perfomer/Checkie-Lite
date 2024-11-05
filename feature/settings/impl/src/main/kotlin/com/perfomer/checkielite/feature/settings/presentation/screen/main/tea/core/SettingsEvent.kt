package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.common.pure.state.Lce

internal sealed interface SettingsEvent {

    data object Initialize : SettingsEvent

    class SyncingStatusUpdated(val isSyncing: Boolean) : SettingsEvent

    class CheckingHasReviewsStatusUpdated(val hasReviews: Boolean) : SettingsEvent

    class UpdatesCheck(val hasUpdates: Lce<Boolean>) : SettingsEvent
}

internal sealed interface SettingsUiEvent : SettingsEvent {

    data object OnBackPress : SettingsUiEvent

    data object OnBackupExportClick : SettingsUiEvent

    data object OnBackupImportClick : SettingsUiEvent

    data object OnBackupImportConfirmClick : SettingsUiEvent

    data object OnCheckUpdatesClick : SettingsUiEvent
}

internal sealed interface SettingsNavigationEvent : SettingsEvent {

    sealed interface BackupFileSelection : SettingsNavigationEvent {
        class Succeed(val path: String) : BackupFileSelection
        data object Canceled : BackupFileSelection
    }
}