package com.perfomer.checkielite.core.data.datasource.database

import androidx.room.withTransaction
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckiePictureDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieTagDao
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagReviewBoundDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDomain
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface DatabaseDataSource {

    fun getReviews(searchQuery: String = ""): Flow<List<CheckieReview>>

    fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

    suspend fun searchBrands(searchQuery: String): List<String>

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

    suspend fun updatePictures(pictures: List<CheckiePicture>)

    suspend fun updateSyncing(reviewId: String, isSyncing: Boolean)

    suspend fun dropSyncing()

    fun getTags(searchQuery: String) : Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun saveTag(tag: CheckieTag)

    suspend fun deleteTag(tagId: String)
}

internal class DatabaseDataSourceImpl(
    private val database: CheckieDatabase,
) : DatabaseDataSource {

    private val reviewDao: CheckieReviewDao by lazy { database.reviewDao() }
    private val pictureDao: CheckiePictureDao by lazy { database.pictureDao() }
    private val tagDao: CheckieTagDao by lazy { database.tagDao() }

    override fun getReviews(searchQuery: String): Flow<List<CheckieReview>> {
        val reviews = if (searchQuery.isBlank()) {
            reviewDao.getReviews()
        } else {
            reviewDao.getReviewsByQuery(searchQuery)
        }

        return reviews.map { reviewDb -> reviewDb.map { it.toDomain() } }
    }

    override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> {
        return reviewDao.getReviewsByBrand(brand)
            .map { reviewDb -> reviewDb.map { it.toDomain() } }
    }

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return reviewDao.getReview(reviewId)
            .map { it.toDomain() }
    }

    override suspend fun searchBrands(searchQuery: String): List<String> {
        return reviewDao.searchBrands(searchQuery)
    }

    override suspend fun createReview(review: CheckieReview) = database.withTransaction {
        val reviewDb = review.toDb()
        val tagsBoundsDb = review.tags.map { tag -> CheckieTagReviewBoundDb(tagId = tag.id, reviewId = review.id) }
        val picturesDb = review.pictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = review.id, order = i)
        }

        reviewDao.insertReview(reviewDb)

        pictureDao.insertPictures(picturesDb)

        tagDao.deleteBoundsForReview(review.id)
        tagDao.insertBounds(tagsBoundsDb)
    }

    override suspend fun updateReview(
        review: CheckieReview,
        deletedPictures: List<CheckiePicture>,
        actualPictures: List<CheckiePicture>
    ) = database.withTransaction {
        val reviewDb = review.toDb()
        val tagsBoundsDb = review.tags.map { tag -> CheckieTagReviewBoundDb(tagId = tag.id, reviewId = review.id) }
        val deletedPicturesIds = deletedPictures.map { it.id }
        val actualPicturesDb = actualPictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = review.id, order = i)
        }

        reviewDao.updateReview(reviewDb)

        pictureDao.insertPictures(actualPicturesDb)
        pictureDao.deletePictures(deletedPicturesIds)

        tagDao.deleteBoundsForReview(review.id)
        tagDao.insertBounds(tagsBoundsDb)
    }

    override suspend fun deleteReview(reviewId: String, deletedPictures: List<CheckiePicture>) = database.withTransaction {
        val deletedPicturesIds = deletedPictures.map { it.id }

        pictureDao.deletePictures(deletedPicturesIds)
        reviewDao.deleteReview(reviewId)
    }

    override suspend fun updatePictures(pictures: List<CheckiePicture>) = database.withTransaction {
        pictures.forEach { picture ->
            pictureDao.updatePictureUri(picture.id, picture.uri)
        }
    }

    override suspend fun updateSyncing(reviewId: String, isSyncing: Boolean) {
        reviewDao.updateSyncing(reviewId, isSyncing)
    }

    override suspend fun dropSyncing() {
        reviewDao.dropSyncing()
    }

    override fun getTags(searchQuery: String): Flow<List<CheckieTag>> {
        val tags = if (searchQuery.isBlank()) {
            tagDao.getTags()
        } else {
            tagDao.getTagsByQuery(searchQuery)
        }

        return tags.map { tagsDb -> tagsDb.map { it.toDomain() } }
    }

    override suspend fun getTag(id: String): CheckieTag {
        return tagDao.getTag(id).toDomain()
    }

    override suspend fun saveTag(tag: CheckieTag) {
        tagDao.insertTag(tag.toDb())
    }

    override suspend fun deleteTag(tagId: String) {
        tagDao.deleteTag(tagId)
    }
}