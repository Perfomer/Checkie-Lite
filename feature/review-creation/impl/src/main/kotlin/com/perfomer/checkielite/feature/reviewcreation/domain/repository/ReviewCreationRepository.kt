package com.perfomer.checkielite.feature.reviewcreation.domain.repository

import com.perfomer.checkielite.core.entity.CheckieReview

internal interface ReviewCreationRepository {

    suspend fun saveReview(review: CheckieReview)

    suspend fun getReview(reviewId: String): CheckieReview
}