package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.coroutines.flow.Flow

interface BrandRepository {

    suspend fun getAllBrands(): List<String>

    fun getRecommendations(reviewId: String): Flow<List<CheckieReview>>
}