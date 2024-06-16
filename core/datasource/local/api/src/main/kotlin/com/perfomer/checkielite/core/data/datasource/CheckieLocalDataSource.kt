package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getReviews(): Flow<List<CheckieReview>>

    fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(reviewId: String)

    suspend fun clearRecentSearches()

    suspend fun getAllBrands(): List<String>

    suspend fun createReview(
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
    )

    suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
    )

    suspend fun deleteReview(reviewId: String)

    suspend fun dropSyncing()

    fun getTags(
        searchQuery: String = "",
        maxCount: Int = Int.MAX_VALUE
    ): Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun getTagByName(name: String): CheckieTag?

    suspend fun createTag(value: String, emoji: String?): CheckieTag

    suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag

    suspend fun deleteTag(id: String)

    suspend fun getCurrencies(): List<CheckieCurrency>
}