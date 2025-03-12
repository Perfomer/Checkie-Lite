package com.perfomer.checkielite.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>
}