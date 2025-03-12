package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>
}