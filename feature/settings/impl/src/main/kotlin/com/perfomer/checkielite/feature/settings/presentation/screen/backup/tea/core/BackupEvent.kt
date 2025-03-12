package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand.Await

internal sealed interface BackupEvent {

    data object Initialize : BackupEvent

    class BackupProgressUpdated(val progress: BackupProgress) : BackupEvent

    class AwaitCompleted(val reason: Await.Reason) : BackupEvent
}

internal sealed interface BackupUiEvent : BackupEvent {

    data object OnBackPress : BackupUiEvent

    data object OnCancelClick : BackupUiEvent
}

internal sealed interface BackupNavigationEvent : BackupEvent