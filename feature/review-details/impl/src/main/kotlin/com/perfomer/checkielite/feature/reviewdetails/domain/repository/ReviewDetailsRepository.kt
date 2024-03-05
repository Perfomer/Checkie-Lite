package com.perfomer.checkielite.feature.reviewdetails.domain.repository

import com.perfomer.checkielite.core.entity.CheckieReview

internal interface ReviewDetailsRepository {

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun deleteReview(reviewId: String)
}