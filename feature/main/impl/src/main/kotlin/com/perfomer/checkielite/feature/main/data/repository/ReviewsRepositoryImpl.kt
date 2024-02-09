package com.perfomer.checkielite.feature.main.data.repository

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository

internal class ReviewsRepositoryImpl(
    private val localDataSource: CheckieLocalDataSource,
) : ReviewsRepository {

    override suspend fun getCheckies(searchQuery: String): List<CheckieReview> {
        return localDataSource.getReviews()
    }
}