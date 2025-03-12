package com.perfomer.checkielite.core.data.repository

import android.content.Context
import com.perfomer.checkielite.common.pure.util.forEachAsync
import com.perfomer.checkielite.common.pure.util.randomUuid
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.core.data.datasource.database.DatabaseDataSource
import com.perfomer.checkielite.core.data.datasource.file.FileDataSource
import com.perfomer.checkielite.core.data.datasource.preferences.PreferencesDataSource
import com.perfomer.checkielite.core.data.service.CompressorService
import com.perfomer.checkielite.core.data.util.startForegroundServiceCompat
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.PictureSource
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date

internal class ReviewRepositoryImpl(
    private val context: Context,
    private val databaseDataSource: DatabaseDataSource,
    private val fileDataSource: FileDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : ReviewRepository {

    override fun getReviews(): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviews()
    }

    override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> {
        return databaseDataSource.getReviewsByBrand(brand)
    }

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return databaseDataSource.getReview(reviewId)
    }

    override suspend fun createReview(
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ) {
        val creationDate = Date()

        val isNeedSync = pictures.isNotEmpty()
        val reviewId = randomUuid()

        databaseDataSource.createReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            price = price,
            rating = rating,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            pictures = pictures,
            tagsIds = tagsIds,
            creationDate = creationDate,
            modificationDate = creationDate,
            isSyncing = isNeedSync,
        )

        if (price != null) {
            preferencesDataSource.setLatestCurrency(price.currency)
        }

        if (isNeedSync) {
            val intent = CompressorService.createIntent(context, reviewId, pictures.toArrayList())
            context.startForegroundServiceCompat(intent)
        }
    }

    override suspend fun updateReview(
        reviewId: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?
    ) = coroutineScope {
        val initialReview = getReview(reviewId).first()
        val initialPictures = initialReview.pictures

        val deletedPictures = initialPictures.filterNot(pictures::contains)
        deletedPictures.forEachAsync { picture -> fileDataSource.deleteFile(picture.uri) }

        val addedPictures = pictures.filter { picture -> picture.source == PictureSource.GALLERY }
        val actualPictures = mutableListOf<CheckiePicture>()

        for (picture in pictures) {
            val isPictureDeleted = deletedPictures.contains(picture)
            if (isPictureDeleted) continue

            val addedPictureIndex = addedPictures.indexOf(picture)
            val isPictureAdded = addedPictureIndex != -1

            actualPictures +=
                if (isPictureAdded) addedPictures[addedPictureIndex]
                else picture
        }

        val isNeedSync = addedPictures.isNotEmpty()

        databaseDataSource.updateReview(
            id = reviewId,
            productName = productName,
            productBrand = productBrand,
            price = price,
            rating = rating,
            deletedPictures = deletedPictures,
            actualPictures = actualPictures,
            comment = comment,
            advantages = advantages,
            disadvantages = disadvantages,
            tagsIds = tagsIds,
            creationDate = initialReview.creationDate,
            modificationDate = Date(),
            isSyncing = isNeedSync,
        )

        if (price != null && initialReview.price?.currency != price.currency) {
            preferencesDataSource.setLatestCurrency(price.currency)
        }

        if (isNeedSync) {
            val intent = CompressorService.createIntent(context, reviewId, addedPictures.toArrayList())
            context.startForegroundServiceCompat(intent)
        }
    }

    override suspend fun deleteReview(reviewId: String) {
        val review = getReview(reviewId).first()
        review.pictures.forEachAsync { picture ->
            fileDataSource.deleteFile(picture.uri)
        }
        databaseDataSource.deleteReview(reviewId, review.pictures)
    }
}