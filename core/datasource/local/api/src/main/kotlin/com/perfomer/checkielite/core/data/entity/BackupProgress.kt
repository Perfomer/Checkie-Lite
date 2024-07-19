package com.perfomer.checkielite.core.data.entity

sealed interface BackupProgress {

    data object None : BackupProgress

    class InProgress(val progress: Float) : BackupProgress

    data object Completed : BackupProgress

    class Failure(val error: Throwable) : BackupProgress
}