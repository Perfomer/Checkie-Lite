package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.core.entity.CheckieReview

interface CheckieLocalDataSource {

    suspend fun getReviews(searchQuery: String = ""): List<CheckieReview>

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun createReview(review: CheckieReview)

    suspend fun updateReview(review: CheckieReview)

    suspend fun deleteReview(reviewId: String)
}