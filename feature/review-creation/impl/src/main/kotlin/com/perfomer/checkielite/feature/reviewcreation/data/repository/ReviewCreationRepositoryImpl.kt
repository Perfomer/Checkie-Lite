package com.perfomer.checkielite.feature.reviewcreation.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import kotlinx.coroutines.flow.first

internal class ReviewCreationRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewCreationRepository {

    override suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?,
    ) {
        localDataSource.createReview(
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            pictures = pictures,
            reviewText = reviewText,
        )
    }

    override suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?,
    ) {
        localDataSource.updateReview(
            reviewId = reviewId,
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            pictures = pictures,
            reviewText = reviewText,
        )
    }

    override suspend fun getReview(reviewId: String): CheckieReview {
        return localDataSource.getReview(reviewId).first()
    }
}