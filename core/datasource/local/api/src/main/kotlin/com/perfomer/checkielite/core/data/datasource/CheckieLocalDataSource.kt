package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckieReview

interface CheckieLocalDataSource {

    suspend fun getReviews(searchQuery: String = ""): List<CheckieReview>

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        picturesUri: List<String>,
        reviewText: String?,
    ) : CheckieReview

    suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        picturesUri: List<String>,
        reviewText: String?,
    ) : CheckieReview

    suspend fun deleteReview(reviewId: String)
}