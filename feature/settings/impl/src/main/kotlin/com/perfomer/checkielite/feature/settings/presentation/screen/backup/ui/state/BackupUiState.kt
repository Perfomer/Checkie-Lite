package com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text

@Immutable
internal data class BackupUiState(
    val title: Text,
    val progressLabel: Text,
    val backupProgress: Float,
    val progressBarStyle: BackupProgressBarStyle,
    val isCancelAvailable: Boolean,
)

internal enum class BackupProgressBarStyle {
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    FAILED,
}
