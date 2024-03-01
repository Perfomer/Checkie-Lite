package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.common.pure.util.mapAsync
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.entity.CheckieReview

internal class CheckieLocalDataSourceImpl(
    private val databaseDataSource: DatabaseDataSource,
    private val fileDataSource: FileDataSource,
) : CheckieLocalDataSource {

    override suspend fun getReviews(searchQuery: String): List<CheckieReview> {
        return databaseDataSource.getReviews(searchQuery)
    }

    override suspend fun getReview(reviewId: String): CheckieReview {
        return databaseDataSource.getReview(reviewId)
    }

    override suspend fun saveReview(review: CheckieReview) {
        val picturesUri = review.picturesUri.mapAsync { uri ->
            fileDataSource.cacheCompressedPicture(uri)
        }

        databaseDataSource.createReview(
            review.copy(picturesUri = picturesUri)
        )
    }

    override suspend fun deleteReview(reviewId: String) {
        TODO("Not yet implemented")
    }
}