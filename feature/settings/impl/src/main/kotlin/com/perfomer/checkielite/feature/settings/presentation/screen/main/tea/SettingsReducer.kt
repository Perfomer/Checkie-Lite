package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea

import android.util.Log
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.CheckSyncing
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ExportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.ImportBackup
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsCommand.LoadSettings
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.BackupExporting
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.BackupImporting
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.Initialize
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsEvent.SyncingStatusUpdated
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.RestartApp
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationCommand.SelectBackupFile
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsNavigationEvent.BackupFileSelection
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupExportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.SettingsUiEvent.OnBackupImportClick
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core.WarningReason

internal class SettingsReducer : DslReducer<SettingsCommand, SettingsEffect, SettingsEvent, SettingsState>() {

    override fun reduce(event: SettingsEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is SettingsUiEvent -> reduceUi(event)
        is SettingsNavigationEvent -> reduceNavigation(event)
        is BackupExporting -> reduceBackupExporting(event)
        is BackupImporting -> reduceBackupImporting(event)
        is SyncingStatusUpdated -> state { copy(isSyncingInProgress = event.isSyncing) }
    }

    private fun reduceInitialize() {
        commands(LoadSettings, CheckSyncing)
    }

    private fun reduceUi(event: SettingsUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnBackupExportClick -> {
            when {
                state.isBackupInProgress -> effects(ShowToast.Warning(WarningReason.BACKUP_IN_PROGRESS))
                state.isSyncingInProgress -> effects(ShowToast.Warning(WarningReason.SYNCING_IN_PROGRESS))
                else -> commands(ExportBackup)
            }
        }
        is OnBackupImportClick -> {
            when {
                state.isBackupInProgress -> effects(ShowToast.Warning(WarningReason.BACKUP_IN_PROGRESS))
                state.isSyncingInProgress -> effects(ShowToast.Warning(WarningReason.SYNCING_IN_PROGRESS))
                else -> commands(SelectBackupFile)
            }
        }
    }

    private fun reduceNavigation(event: SettingsNavigationEvent) = when (event) {
        is BackupFileSelection -> reduceBackupFileSelection(event)
    }

    private fun reduceBackupExporting(event: BackupExporting) = when (event) {
        is BackupExporting.Started -> {
            state { copy(isExportingInProgress = true) }
        }
        is BackupExporting.Succeed -> {
            state { copy(isExportingInProgress = false) }
        }
        is BackupExporting.Failed -> {
            Log.e(TAG, "Failed to export backup", event.error) // todo move to Actor
            state { copy(isExportingInProgress = false) }
            effects(ShowToast.Error)
        }
    }

    private fun reduceBackupImporting(event: BackupImporting) = when (event) {
        is BackupImporting.Started -> {
            state { copy(isImportingInProgress = true) }
        }
        is BackupImporting.Succeed -> {
            state { copy(isImportingInProgress = false) }
            commands(RestartApp)
        }
        is BackupImporting.Failed -> {
            Log.e(TAG, "Failed to import backup", event.error)
            state { copy(isImportingInProgress = false) }
            effects(ShowToast.Error)
        }
    }

    private fun reduceBackupFileSelection(event: BackupFileSelection) = when (event) {
        is BackupFileSelection.Succeed -> commands(ImportBackup(event.path))
        is BackupFileSelection.Canceled -> Unit
    }

    private companion object {
        private const val TAG = "SettingsReducer"
    }
}