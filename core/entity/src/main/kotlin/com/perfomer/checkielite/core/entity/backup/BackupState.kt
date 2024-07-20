package com.perfomer.checkielite.core.entity.backup

class BackupState(
    val mode: BackupMode?,
    val progress: BackupProgress,
) {
    companion object {
        val default: BackupState = BackupState(null, BackupProgress.None)
    }
}