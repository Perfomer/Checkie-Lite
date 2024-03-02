package com.perfomer.checkielite.feature.reviewdetails.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewRepository

internal class ReviewRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewRepository {

    override suspend fun getReview(reviewId: String): CheckieReview {
        return localDataSource.getReview(reviewId)
    }

    override suspend fun deleteReview(reviewId: String) {
        localDataSource.deleteReview(reviewId)
    }
}