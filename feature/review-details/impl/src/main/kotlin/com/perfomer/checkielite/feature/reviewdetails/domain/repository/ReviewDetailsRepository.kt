package com.perfomer.checkielite.feature.reviewdetails.domain.repository

import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow

internal interface ReviewDetailsRepository {

    fun getReview(reviewId: String): Flow<CheckieReview>

    fun getRecommendations(reviewId: String) : Flow<List<CheckieReview>>

    suspend fun deleteReview(reviewId: String)
}