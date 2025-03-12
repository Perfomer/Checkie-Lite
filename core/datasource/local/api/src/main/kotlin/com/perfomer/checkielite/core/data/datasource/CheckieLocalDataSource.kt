package com.perfomer.checkielite.core.data.datasource

import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {


    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>
}