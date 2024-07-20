package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress

internal data class BackupState(
    val mode: BackupMode,
    val backupState: BackupProgress = BackupProgress.None,
    val progressValue: Float = 0F,
)