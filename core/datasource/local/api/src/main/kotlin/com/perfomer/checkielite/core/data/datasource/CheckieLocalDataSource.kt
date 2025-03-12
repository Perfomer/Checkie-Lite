package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>

    fun getTags(
        searchQuery: String = "",
        maxCount: Int = Int.MAX_VALUE
    ): Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun getTagByName(name: String): CheckieTag?

    suspend fun createTag(value: String, emoji: String?): CheckieTag

    suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag

    suspend fun deleteTag(id: String)

    suspend fun getAllCurrenciesCodes(): List<String>

    suspend fun getLatestCurrency(): CheckieCurrency?

    suspend fun getLatestTagSortingStrategy(): TagSortingStrategy?

    suspend fun setLatestTagSortingStrategy(strategy: TagSortingStrategy)
}