package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.entity.backup.BackupMode
import com.perfomer.checkielite.core.entity.backup.BackupProgress

internal data class BackupState(
    val mode: BackupMode,
    val progressValue: Float = 0F,
    val backupProgress: BackupProgress = BackupProgress.None,
    val isCancelled: Boolean = false,
)