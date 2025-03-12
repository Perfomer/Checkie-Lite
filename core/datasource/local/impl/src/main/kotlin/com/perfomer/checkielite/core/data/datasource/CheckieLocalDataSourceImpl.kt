package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import kotlinx.coroutines.flow.Flow

internal class CheckieLocalDataSourceImpl(
    private val databaseDataSource: DatabaseDataSource,
) : CheckieLocalDataSource {

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override fun isSyncing(): Flow<Boolean> {
        return databaseDataSource.isSyncing()
    }
}