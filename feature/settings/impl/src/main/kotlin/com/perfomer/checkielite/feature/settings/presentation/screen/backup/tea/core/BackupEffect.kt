package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core

import com.perfomer.checkielite.core.entity.backup.BackupMode

internal sealed interface BackupEffect {

    class ShowErrorToast(val mode: BackupMode) : BackupEffect
}
