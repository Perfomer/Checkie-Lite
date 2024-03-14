package com.perfomer.checkielite.feature.reviewcreation.domain.repository

import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview

internal interface ReviewCreationRepository {

    suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?
    )

    suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        reviewText: String?
    )

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun searchBrands(query: String) : List<String>
}