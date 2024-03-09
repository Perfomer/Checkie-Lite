package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow

interface CheckieLocalDataSource {

    fun getReviews(searchQuery: String = ""): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

    suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?,
    ): CheckieReview

    suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?,
    ): CheckieReview

    suspend fun deleteReview(reviewId: String)
}