package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.price.CheckiePrice
import com.perfomer.checkielite.core.domain.entity.review.CheckiePicture
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun getReviews(): Flow<List<CheckieReview>>

    fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

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
}