package com.perfomer.checkielite.core.data.repository

import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class BrandRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource,
) : BrandRepository {

    override fun getRecommendations(reviewId: String): Flow<List<CheckieReview>> {
        return databaseDataSource.getReview(reviewId)
            .flatMapLatest { review ->
                if (review.productBrand.isNullOrBlank()) {
                    flowOf(emptyList())
                } else {
                    databaseDataSource.getReviewsByBrand(review.productBrand!!)
                        .map { reviews ->
                            reviews.filterNot { it.id == reviewId }
                        }
                }
            }
    }
}