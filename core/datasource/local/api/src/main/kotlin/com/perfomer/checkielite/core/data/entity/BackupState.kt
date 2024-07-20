package com.perfomer.checkielite.core.data.entity

class BackupState(
    val mode: BackupMode,
    val progress: BackupProgress,
) {
    companion object {
        val default: BackupState = BackupState(BackupMode.NONE, BackupProgress.None)
    }
}