package com.perfomer.checkielite.feature.reviewdetails.domain.repository

import com.perfomer.checkielite.core.entity.CheckieReview

internal interface ReviewRepository {

    suspend fun getReview(reviewId: String): CheckieReview
}