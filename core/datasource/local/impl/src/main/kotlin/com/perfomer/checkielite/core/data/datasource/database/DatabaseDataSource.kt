package com.perfomer.checkielite.core.data.datasource.database

import androidx.room.withTransaction
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDomain
import com.perfomer.checkielite.core.entity.CheckieReview

internal interface DatabaseDataSource {

    suspend fun getReviews(searchQuery: String = ""): List<CheckieReview>

    suspend fun getReview(reviewId: String): CheckieReview

    suspend fun createReview(review: CheckieReview)

    suspend fun updateReview(review: CheckieReview)

    suspend fun deleteReview(reviewId: String)
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

    override suspend fun createReview(review: CheckieReview) {
        val reviewDb = review.toDb()

        database.withTransaction {
            checkieReviewDao.insertReview(reviewDb.review)
            checkieReviewDao.insertPictures(reviewDb.picturesUri)
        }
    }

    override suspend fun updateReview(review: CheckieReview) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReview(reviewId: String) {
        TODO("Not yet implemented")
    }

}