package com.perfomer.checkielite.core.data.datasource

import com.perfomer.checkielite.common.pure.util.forEachAsync
import com.perfomer.checkielite.common.pure.util.mapAsync
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import kotlinx.coroutines.coroutineScope
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
        pictures: List<CheckiePicture>,
        reviewText: String?
    ): CheckieReview {
        val compressedPictures = pictures.mapAsync { picture ->
            CheckiePicture(
                id = randomUuid(),
                uri = fileDataSource.cacheCompressedPicture(picture.uri),
            )
        }

        val creationDate = Date()

        val review = CheckieReview(
            id = randomUuid(),
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            reviewText = reviewText,
            pictures = compressedPictures,
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
        pictures: List<CheckiePicture>,
        reviewText: String?
    ): CheckieReview = coroutineScope {
        val initialReview = getReview(reviewId)
        val initialPictures = initialReview.pictures

        val deletedPictures = initialPictures.filterNot(pictures::contains)
        deletedPictures.forEachAsync { picture -> fileDataSource.deleteFile(picture.uri) }

        val addedPictures = pictures.filter { picture -> picture.id == CheckiePicture.NO_ID }
        val compressedAddedPictures = addedPictures.mapAsync { picture ->
            CheckiePicture(
                id = randomUuid(),
                uri = fileDataSource.cacheCompressedPicture(picture.uri),
            )
        }

        val actualPictures = mutableListOf<CheckiePicture>()

        pictures.forEach { picture ->
            val isPictureDeleted = deletedPictures.contains(picture)
            if (isPictureDeleted) return@forEach

            val addedPictureIndex = addedPictures.indexOf(picture)
            val isPictureAdded = addedPictureIndex != -1

            actualPictures +=
                if (isPictureAdded) compressedAddedPictures[addedPictureIndex]
                else picture
        }

        val review = CheckieReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            reviewText = reviewText,
            pictures = actualPictures,
            creationDate = initialReview.creationDate,
            modificationDate = Date(),
        )

        databaseDataSource.updateReview(
            review = review,
            deletedPictures = deletedPictures,
            actualPictures = actualPictures,
        )

        return@coroutineScope review
    }

    override suspend fun deleteReview(reviewId: String) {
        val review = getReview(reviewId)
        review.pictures.forEachAsync { picture ->
            fileDataSource.deleteFile(picture.uri)
        }
        databaseDataSource.deleteReview(reviewId, review.pictures)
    }
}