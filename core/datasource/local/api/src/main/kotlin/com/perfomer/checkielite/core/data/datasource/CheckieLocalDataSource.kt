package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>

    suspend fun getAllCurrenciesCodes(): List<String>

    suspend fun getLatestCurrency(): CheckieCurrency?

}