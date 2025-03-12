package com.perfomer.checkielite.feature.main.domain.repository

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.coroutines.flow.Flow

internal interface ReviewsRepository {

    fun getCheckies(searchQuery: String = ""): Flow<List<CheckieReview>>
}