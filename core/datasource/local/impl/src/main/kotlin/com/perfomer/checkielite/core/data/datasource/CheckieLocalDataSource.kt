package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.common.pure.util.forEachAsync
import com.perfomer.checkielite.common.pure.util.mapAsync
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.entity.CheckieReview
import java.util.Date

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

    override suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        picturesUri: List<String>,
        reviewText: String?
    ): CheckieReview {
        val compressedPicturesUri = picturesUri.mapAsync { uri ->
            fileDataSource.cacheCompressedPicture(uri)
        }

        val creationDate = Date()

        val review = CheckieReview(
            id = randomUuid(),
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            reviewText = reviewText,
            picturesUri = compressedPicturesUri,
            creationDate = creationDate,
            modificationDate = creationDate,
        )

        databaseDataSource.createReview(review)

        return review
    }

    override suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        picturesUri: List<String>,
        reviewText: String?
    ): CheckieReview {
        val initialReview = getReview(reviewId)

        val deletedPictures = initialReview.picturesUri.filterNot(picturesUri::contains)
        deletedPictures.forEachAsync { uri -> fileDataSource.deleteFile(uri) } // TODO: check if other reviews have the same picture

        val addedPictures = picturesUri.filterNot(initialReview.picturesUri::contains)
        val compressedAddedPictures = addedPictures.mapAsync { uri -> fileDataSource.cacheCompressedPicture(uri) }

        val review = CheckieReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            reviewText = reviewText,
            picturesUri = initialReview.picturesUri - deletedPictures + compressedAddedPictures,
            creationDate = initialReview.creationDate,
            modificationDate = Date(),
        )

        databaseDataSource.updateReview(
            review = review,
            deletedPicturesUri = deletedPictures,
            addedPicturesUri = compressedAddedPictures,
        )

        return review
    }

    override suspend fun deleteReview(reviewId: String) {
        val review = getReview(reviewId)
        review.picturesUri.forEachAsync { uri -> fileDataSource.deleteFile(uri) } // TODO: check if other reviews have the same picture
        databaseDataSource.deleteReview(reviewId, review.picturesUri)
    }
}