package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import kotlinx.coroutines.flow.Flow

internal class AppRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
) : AppRepository {

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override fun isSyncing(): Flow<Boolean> {
        return databaseDataSource.isSyncing()
    }
}