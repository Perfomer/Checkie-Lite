package com.perfomer.checkielite.core.data.datasource

import android.content.Context
import androidx.core.content.ContextCompat
import com.perfomer.checkielite.common.pure.util.forEachAsync
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date

internal class CheckieLocalDataSourceImpl(
    private val context: Context,
    private val databaseDataSource: DatabaseDataSource,
    private val fileDataSource: FileDataSource,
) : CheckieLocalDataSource {

    override fun getReviews(searchQuery: String): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviews(searchQuery)
    }

    override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviewsByBrand(brand)
    }

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return databaseDataSource.getReview(reviewId)
    }

    override suspend fun searchBrands(searchQuery: String): List<String> {
        return databaseDataSource.searchBrands(searchQuery)
    }

    override suspend fun createReview(
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tags: List<CheckieTag>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ): CheckieReview {
        val actualPictures = pictures.map { picture -> picture.copy(id = randomUuid()) }
        val creationDate = Date()

        val isNeedSync = actualPictures.isNotEmpty()

        val review = CheckieReview(
            id = randomUuid(),
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            pictures = actualPictures,
            tags = tags,
            creationDate = creationDate,
            modificationDate = creationDate,
            isSyncing = isNeedSync,
        )

        databaseDataSource.createReview(review)

        if (isNeedSync) {
            ContextCompat.startForegroundService(context, CompressorService.createIntent(context, review.id, actualPictures.toArrayList()))
        }

        return review
    }

    override suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tags: List<CheckieTag>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ): CheckieReview = coroutineScope {
        val initialReview = getReview(reviewId).first()
        val initialPictures = initialReview.pictures

        val deletedPictures = initialPictures.filterNot(pictures::contains)
        deletedPictures.forEachAsync { picture -> fileDataSource.deleteFile(picture.uri) }

        val addedPictures = pictures.filter { picture -> picture.id == CheckiePicture.NO_ID }
        val actualAddedPictures = addedPictures.map { picture -> picture.copy(id = randomUuid()) }

        val actualPictures = mutableListOf<CheckiePicture>()

        pictures.forEach { picture ->
            val isPictureDeleted = deletedPictures.contains(picture)
            if (isPictureDeleted) return@forEach

            val addedPictureIndex = addedPictures.indexOf(picture)
            val isPictureAdded = addedPictureIndex != -1

            actualPictures +=
                if (isPictureAdded) actualAddedPictures[addedPictureIndex]
                else picture
        }

        val isNeedSync = addedPictures.isNotEmpty()

        val review = CheckieReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            rating = rating,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            pictures = actualPictures,
            tags = tags,
            creationDate = initialReview.creationDate,
            modificationDate = Date(),
            isSyncing = isNeedSync,
        )

        databaseDataSource.updateReview(
            review = review,
            deletedPictures = deletedPictures,
            actualPictures = actualPictures,
        )

        if (isNeedSync) {
            ContextCompat.startForegroundService(context, CompressorService.createIntent(context, review.id, actualAddedPictures.toArrayList()))
        }

        return@coroutineScope review
    }

    override suspend fun deleteReview(reviewId: String) {
        val review = getReview(reviewId).first()
        review.pictures.forEachAsync { picture ->
            fileDataSource.deleteFile(picture.uri)
        }
        databaseDataSource.deleteReview(reviewId, review.pictures)
    }

    override suspend fun dropSyncing() {
        databaseDataSource.dropSyncing()
    }

    override suspend fun getTag(id: String): CheckieTag {
        return databaseDataSource.getTag(id)
    }

    override suspend fun createTag(value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = randomUuid(), value = value, emoji = emoji)
        databaseDataSource.saveTag(tag)

        return tag
    }

    override suspend fun updateTag(id: String, value: String, emoji: String?): CheckieTag {
        val tag = CheckieTag(id = id, value = value, emoji = emoji)
        databaseDataSource.saveTag(tag)

        return tag
    }

    override suspend fun deleteTag(id: String) {
        databaseDataSource.deleteTag(id)
    }
}