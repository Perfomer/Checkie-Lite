package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy
import kotlinx.coroutines.flow.Flow
import java.util.Date

internal class SearchRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val preferencesDataSource: PreferencesDataSource,
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

    override suspend fun getLatestTagSearchSortingStrategy(): ReviewsSortingStrategy {
        return preferencesDataSource.getLatestTagSearchSortingStrategy()
    }

    override suspend fun setLatestTagSearchSortingStrategy(strategy: ReviewsSortingStrategy) {
        preferencesDataSource.setLatestTagSearchSortingStrategy(strategy)
    }
}
