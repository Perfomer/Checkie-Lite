package com.perfomer.checkielite.navigation

import com.perfomer.checkielite.core.entity.backup.BackupProgress
import com.perfomer.checkielite.core.navigation.api.CheckieScreen
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupScreenProvider

internal class StartScreenProvider(
    private val backupNavigationManager: BackupNavigationManager,
    private val backupScreenProvider: BackupScreenProvider,
    private val mainScreenProvider: MainScreenProvider,
) {

    operator fun invoke(): CheckieScreen {
        val backupState = backupNavigationManager.getCurrentBackupState()

        val backupMode = backupState.mode
        val isBackupInProgress = backupState.progress is BackupProgress.InProgress

        return if (isBackupInProgress && backupMode != null) {
            backupScreenProvider(BackupParams(backupMode))
        } else {
            mainScreenProvider()
        }
    }
}