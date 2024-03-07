package com.perfomer.checkielite.core.data.datasource.database

import androidx.room.withTransaction
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDomain
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview

internal interface DatabaseDataSource {

    suspend fun getReviews(searchQuery: String = ""): List<CheckieReview>

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun createReview(review: CheckieReview)

    suspend fun updateReview(
        review: CheckieReview,
        deletedPictures: List<CheckiePicture>,
        actualPictures: List<CheckiePicture>,
    )

    suspend fun deleteReview(
        reviewId: String,
        deletedPictures: List<CheckiePicture>,
    )
}

internal class DatabaseDataSourceImpl(
    private val database: CheckieDatabase,
) : DatabaseDataSource {

    private val checkieReviewDao: CheckieReviewDao
        get() = database.reviewDao()

    override suspend fun getReviews(searchQuery: String): List<CheckieReview> {
        val reviews = if (searchQuery.isBlank()) {
            checkieReviewDao.getReviews()
        } else {
            checkieReviewDao.getReviewsByQuery(searchQuery)
        }

        return reviews.map { it.toDomain() }
    }

    override suspend fun getReview(reviewId: String): CheckieReview {
        return checkieReviewDao.getReview(reviewId).toDomain()
    }

    override suspend fun createReview(review: CheckieReview) = database.withTransaction {
        val reviewDb = review.toDb()
        val picturesDb = review.pictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = review.id, order = i)
        }

        checkieReviewDao.insertReview(reviewDb)
        checkieReviewDao.insertPictures(picturesDb)
    }

    override suspend fun updateReview(
        review: CheckieReview,
        deletedPictures: List<CheckiePicture>,
        actualPictures: List<CheckiePicture>
    ) = database.withTransaction {
        val reviewDb = review.toDb()
        val deletedPicturesIds = deletedPictures.map { it.id }
        val actualPicturesDb = actualPictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = review.id, order = i)
        }

        checkieReviewDao.updateReview(reviewDb)
        checkieReviewDao.insertPictures(actualPicturesDb)
        checkieReviewDao.deletePictures(deletedPicturesIds)
    }

    override suspend fun deleteReview(reviewId: String, deletedPictures: List<CheckiePicture>) = database.withTransaction {
        val deletedPicturesIds = deletedPictures.map { it.id }

        checkieReviewDao.deletePictures(deletedPicturesIds)
        checkieReviewDao.deleteReview(reviewId)
    }
}