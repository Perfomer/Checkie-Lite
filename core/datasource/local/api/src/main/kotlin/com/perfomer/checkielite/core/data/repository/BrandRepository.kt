package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow

interface BrandRepository {

    fun getRecommendations(reviewId: String): Flow<List<CheckieReview>>
}