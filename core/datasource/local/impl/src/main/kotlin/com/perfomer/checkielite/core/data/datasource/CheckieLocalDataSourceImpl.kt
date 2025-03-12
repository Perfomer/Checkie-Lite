package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow
import java.util.Date

internal class CheckieLocalDataSourceImpl(
    private val databaseDataSource: DatabaseDataSource,
) : CheckieLocalDataSource {

    override fun getRecentSearches(): Flow<List<CheckieReview>> {
        return databaseDataSource.getRecentSearches()
    }

    override suspend fun rememberRecentSearch(reviewId: String) {
        return databaseDataSource.rememberRecentSearch(
            reviewId = reviewId,
            searchDate = Date(),
        )
    }

    override suspend fun clearRecentSearches() {
        return databaseDataSource.clearRecentSearches()
    }

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override fun isSyncing(): Flow<Boolean> {
        return databaseDataSource.isSyncing()
    }
}