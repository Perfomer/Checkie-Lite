package com.perfomer.checkielite.core.data.datasource.database

import androidx.room.withTransaction
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDomain
import com.perfomer.checkielite.core.entity.CheckieReview

internal interface DatabaseDataSource {

    suspend fun getReviews(searchQuery: String = ""): List<CheckieReview>

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun createReview(review: CheckieReview)

    suspend fun updateReview(
        review: CheckieReview,
        deletedPicturesUri: List<String>,
        addedPicturesUri: List<String>,
    )

    suspend fun deleteReview(
        reviewId: String,
        deletedPicturesUri: List<String>,
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
        val picturesDb = review.picturesUri.map { uri -> CheckieReviewPictureDb(reviewId = review.id, uri = uri) }

        checkieReviewDao.insertReview(reviewDb)
        checkieReviewDao.insertPictures(picturesDb)
    }

    override suspend fun updateReview(
        review: CheckieReview,
        deletedPicturesUri: List<String>,
        addedPicturesUri: List<String>
    ) = database.withTransaction {
        val reviewDb = review.toDb()
        val addedPicturesDb = addedPicturesUri.map { uri -> CheckieReviewPictureDb(reviewId = review.id, uri = uri) }

        checkieReviewDao.updateReview(reviewDb)
        checkieReviewDao.insertPictures(addedPicturesDb)
        checkieReviewDao.deletePictures(deletedPicturesUri)
    }

    override suspend fun deleteReview(reviewId: String, deletedPicturesUri: List<String>) = database.withTransaction {
        checkieReviewDao.deleteReview(reviewId)
        checkieReviewDao.deletePictures(deletedPicturesUri)
    }
}