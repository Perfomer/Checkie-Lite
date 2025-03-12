package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.coroutines.flow.Flow
import java.util.Date

internal class SearchRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
) : SearchRepository {

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
}