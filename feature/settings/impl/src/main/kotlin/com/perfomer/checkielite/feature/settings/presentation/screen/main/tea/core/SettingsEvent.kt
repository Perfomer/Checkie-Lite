package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode
import java.util.Locale

internal sealed interface SettingsEvent {

    data object Initialize : SettingsEvent

    class SyncingStatusUpdated(val isSyncing: Boolean) : SettingsEvent

    class CheckingHasReviewsStatusUpdated(val hasReviews: Boolean) : SettingsEvent

    class UpdatesCheck(val hasUpdates: Lce<Boolean>) : SettingsEvent

    class CurrentLocaleUpdated(val locale: Locale) : SettingsEvent

    class ThemeUpdated(val theme: ThemeMode) : SettingsEvent

    class LiquidGlassUpdated(val enabled: Boolean) : SettingsEvent

    class LiquidGlassSaved(val enabled: Boolean) : SettingsEvent

    data object LiquidGlassSaveFailed : SettingsEvent
}

internal sealed interface SettingsUiEvent : SettingsEvent {

    data object OnBackPress : SettingsUiEvent

    data object OnStart : SettingsUiEvent

    data object OnBackupExportClick : SettingsUiEvent

    data object OnBackupImportClick : SettingsUiEvent

    data object OnBackupImportConfirmClick : SettingsUiEvent

    data object OnCheckUpdatesClick : SettingsUiEvent

    data object OnLanguageSettingsClick : SettingsUiEvent

    data object OnLibrariesClick : SettingsUiEvent

    data object OnChangelogClick : SettingsUiEvent

    data object OnThemeSettingsClick : SettingsUiEvent

    class OnLiquidGlassChanged(val enabled: Boolean) : SettingsUiEvent
}

internal sealed interface SettingsNavigationEvent : SettingsEvent {

    sealed interface BackupFileSelection : SettingsNavigationEvent {
        class Succeed(val path: String) : BackupFileSelection
        data object Canceled : BackupFileSelection
    }
}
