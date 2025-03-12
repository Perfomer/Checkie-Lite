package com.perfomer.checkielite.feature.main.data.repository

import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import kotlinx.coroutines.flow.Flow

internal class ReviewsRepositoryImpl(
    private val reviewRepository: ReviewRepository,
) : ReviewsRepository {

    override fun getCheckies(searchQuery: String): Flow<List<CheckieReview>> {
        return reviewRepository.getReviews()
    }
}