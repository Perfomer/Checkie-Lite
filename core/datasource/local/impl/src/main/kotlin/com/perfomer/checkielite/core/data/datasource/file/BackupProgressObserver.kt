package com.perfomer.checkielite.core.data.datasource.file

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface BackupProgressObserver {

    fun observe(): Flow<Float>
}

internal interface BackupProgressNotifier {

    suspend fun notify(progress: Float)
}

internal class BackupProgressObserverImpl : BackupProgressObserver, BackupProgressNotifier {

    private val progressFlow: MutableSharedFlow<Float> = MutableSharedFlow()

    override fun observe(): Flow<Float> {
        return progressFlow
    }

    override suspend fun notify(progress: Float) {
        progressFlow.emit(progress)
    }
}