package com.perfomer.checkielite.core.data.datasource.file.backup

sealed interface BackupProgress {

    data object None : BackupProgress

    class Progress(val progress: Float) : BackupProgress

    data object Success : BackupProgress

    class Failure(val error: Throwable) : BackupProgress
}