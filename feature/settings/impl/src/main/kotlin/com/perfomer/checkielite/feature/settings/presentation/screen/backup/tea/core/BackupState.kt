package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.data.entity.BackupProgress
import com.perfomer.checkielite.feature.settings.presentation.entity.BackupMode

internal data class BackupState(
    val mode: BackupMode,
    val progress: BackupProgress = BackupProgress.None,
)