package com.perfomer.checkielite.core.data.datasource.file.backup

import com.perfomer.checkielite.core.data.entity.BackupProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface BackupProgressObserver {

    fun observe(): Flow<BackupProgress>
}

internal interface BackupProgressNotifier {

    suspend fun notify(progress: BackupProgress)
}

internal class BackupProgressObserverImpl : BackupProgressObserver, BackupProgressNotifier {

    private val progressFlow: MutableSharedFlow<BackupProgress> = MutableSharedFlow()

    override fun observe(): Flow<BackupProgress> {
        return progressFlow
    }

    override suspend fun notify(progress: BackupProgress) {
        progressFlow.emit(progress)
    }
}