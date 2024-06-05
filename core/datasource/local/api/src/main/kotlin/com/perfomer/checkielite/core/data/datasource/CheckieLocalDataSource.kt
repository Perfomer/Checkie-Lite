package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getReviews(): Flow<List<CheckieReview>>

    fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

    suspend fun getRecentSearches(): List<CheckieReview>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun searchBrands(searchQuery: String): List<String>

    suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tags: List<CheckieTag>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
    ): CheckieReview

    suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tags: List<CheckieTag>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
    ): CheckieReview

    suspend fun deleteReview(reviewId: String)

    suspend fun dropSyncing()

    fun getTags(
        searchQuery: String = "",
        maxCount: Int = Int.MAX_VALUE
    ): Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun createTag(value: String, emoji: String?): CheckieTag

    suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag

    suspend fun deleteTag(id: String)
}