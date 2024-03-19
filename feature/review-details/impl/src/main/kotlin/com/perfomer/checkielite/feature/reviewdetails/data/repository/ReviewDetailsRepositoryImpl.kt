package com.perfomer.checkielite.feature.reviewdetails.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class ReviewDetailsRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewDetailsRepository {

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return localDataSource.getReview(reviewId)
    }

    override fun getRecommendations(reviewId: String): Flow<List<CheckieReview>> {
        return localDataSource.getReview(reviewId)
            .flatMapLatest { review ->
                if (review.productBrand.isNullOrBlank()) flowOf(emptyList())
                else localDataSource.getReviewsByBrand(review.productBrand!!)
                    .map { reviews ->
                        reviews.filterNot { it.id == reviewId }
                    }
            }
    }

    override suspend fun deleteReview(reviewId: String) {
        localDataSource.deleteReview(reviewId)
    }
}