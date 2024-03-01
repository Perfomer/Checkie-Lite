package com.perfomer.checkielite.feature.reviewcreation.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository

internal class ReviewCreationRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewCreationRepository {

    override suspend fun saveReview(review: CheckieReview) {
        localDataSource.saveReview(review)
    }

    override suspend fun getReview(reviewId: String): CheckieReview {
        return localDataSource.getReview(reviewId)
    }
}