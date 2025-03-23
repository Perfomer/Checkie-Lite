package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckHasReviews
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckSyncing
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckUpdates
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ExportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ImportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LaunchAppUpdate
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowConfirmImportDialog
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast.Reason
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.CheckingHasReviewsStatusUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.SyncingStatusUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.UpdatesCheck
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.OpenLanguageSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.SelectBackupFile
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.BackupFileSelection
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportConfirmClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnCheckUpdatesClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnLanguageSettingsClick

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

                if (event.hasUpdates.content) commands(LaunchAppUpdate)
                else effects(ShowToast(Reason.APP_IS_UP_TO_DATE))
            }
            is Lce.Error -> {
                state { copy(isCheckUpdatesInProgress = false) }
                effects(ShowToast(Reason.FAILED_TO_CHECK_UPDATES))
            }
        }
    }

    private fun reduceInitialize() {
        commands(LoadSettings, CheckSyncing, CheckHasReviews)
    }

    private fun reduceUi(event: SettingsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnBackupExportClick -> {
            when {
                state.isSyncingInProgress -> effects(ShowToast(Reason.SYNCING_IN_PROGRESS))
                else -> commands(ExportBackup)
            }
        }
        is OnBackupImportClick -> {
            when {
                state.isSyncingInProgress -> effects(ShowToast(Reason.SYNCING_IN_PROGRESS))
                state.hasReviews -> effects(ShowConfirmImportDialog)
                else -> commands(SelectBackupFile)
            }
        }
        is OnBackupImportConfirmClick -> commands(SelectBackupFile)
        is OnCheckUpdatesClick -> commands(CheckUpdates)
        is OnLanguageSettingsClick -> commands(OpenLanguageSettings)
    }

    private fun reduceNavigation(event: SettingsNavigationEvent) = when (event) {
        is BackupFileSelection -> reduceBackupFileSelection(event)
    }

    private fun reduceBackupFileSelection(event: BackupFileSelection) = when (event) {
        is BackupFileSelection.Succeed -> commands(ImportBackup(event.path))
        is BackupFileSelection.Canceled -> Unit
    }
}