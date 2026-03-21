package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun getLatestTagSearchSortingStrategy(): ReviewsSortingStrategy

    suspend fun setLatestTagSearchSortingStrategy(strategy: ReviewsSortingStrategy)
}
