package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class BackupUiState(
    val title: String,
    val progressLabel: String?,
    val backupProgress: Float?,
)
