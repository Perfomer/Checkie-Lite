package com.perfomer.checkielite.feature.reviewdetails.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewDetailsRepository
import kotlinx.coroutines.flow.Flow

internal class ReviewDetailsRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewDetailsRepository {

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return localDataSource.getReview(reviewId)
    }

    override suspend fun deleteReview(reviewId: String) {
        localDataSource.deleteReview(reviewId)
    }
}