package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckHasReviews
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckSyncing
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckUpdates
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ExportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ImportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LaunchAppUpdate
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadCurrentLocale
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadTheme
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowConfirmImportDialog
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.CheckingHasReviewsStatusUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.CurrentLocaleUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.SyncingStatusUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.ThemeUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.UpdatesCheck
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.OpenChangelog
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.OpenLanguageSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.OpenLibraries
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.OpenThemeSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.SelectBackupFile
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.BackupFileSelection
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportConfirmClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnChangelogClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnCheckUpdatesClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnLanguageSettingsClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnLibrariesClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnStart
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnThemeSettingsClick

internal class SettingsReducer : DslReducer<SettingsCommand, SettingsEffect, SettingsEvent, SettingsState>() {

    override fun reduce(event: SettingsEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is SettingsUiEvent -> reduceUi(event)
        is SettingsNavigationEvent -> reduceNavigation(event)
        is SyncingStatusUpdated -> state { copy(isSyncingInProgress = event.isSyncing) }
        is CheckingHasReviewsStatusUpdated -> state { copy(hasReviews = event.hasReviews) }
        is UpdatesCheck -> when (event.hasUpdates) {
            is Lce.Loading -> {
                state { copy(isCheckUpdatesInProgress = true) }
            }
            is Lce.Content -> {
                state { copy(isCheckUpdatesInProgress = false) }

                if (event.hasUpdates.content) {
                    commands(LaunchAppUpdate)
                } else {
                    effects(
                        ShowToast(
                            text = Text.resource(R.string.settings_toast_update_check_succeed),
                            style = ToastStyle.SUCCESS,
                        ),
                    )
                }
            }
            is Lce.Error -> {
                state { copy(isCheckUpdatesInProgress = false) }
                effects(
                    ShowToast(
                        text = Text.resource(R.string.settings_toast_update_check_failed),
                        style = ToastStyle.ERROR,
                    ),
                )
            }
        }
        is CurrentLocaleUpdated -> state { copy(currentLocale = event.locale) }
        is ThemeUpdated -> state { copy(currentTheme = event.theme) }
    }

    private fun reduceInitialize() {
        commands(CheckSyncing, CheckHasReviews, LoadCurrentLocale, LoadTheme)
    }

    private fun reduceUi(event: SettingsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnStart -> commands(LoadCurrentLocale)
        is OnBackupExportClick -> {
            if (state.isSyncingInProgress) {
                effects(
                    ShowToast(
                        text = Text.resource(CommonString.common_toast_syncing),
                        style = ToastStyle.WARNING,
                    ),
                )
            } else {
                commands(ExportBackup)
            }
        }
        is OnBackupImportClick -> {
            when {
                state.isSyncingInProgress -> effects(
                    ShowToast(
                        text = Text.resource(CommonString.common_toast_syncing),
                        style = ToastStyle.WARNING,
                    ),
                )
                state.hasReviews -> effects(ShowConfirmImportDialog)
                else -> commands(SelectBackupFile)
            }
        }
        is OnBackupImportConfirmClick -> commands(SelectBackupFile)
        is OnChangelogClick -> commands(OpenChangelog)
        is OnCheckUpdatesClick -> commands(CheckUpdates)
        is OnLanguageSettingsClick -> commands(OpenLanguageSettings)
        is OnLibrariesClick -> commands(OpenLibraries)
        is OnThemeSettingsClick -> commands(OpenThemeSettings(state.currentTheme))
    }

    private fun reduceNavigation(event: SettingsNavigationEvent) = when (event) {
        is BackupFileSelection -> reduceBackupFileSelection(event)
    }

    private fun reduceBackupFileSelection(event: BackupFileSelection) = when (event) {
        is BackupFileSelection.Succeed -> commands(ImportBackup(event.path))
        is BackupFileSelection.Canceled -> Unit
    }
}
