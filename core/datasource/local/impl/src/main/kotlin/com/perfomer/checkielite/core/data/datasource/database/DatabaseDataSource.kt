package com.perfomer.checkielite.core.data.datasource.database

import android.content.Context
import androidx.room.withTransaction
import com.perfomer.checkielite.core.data.datasource.database.room.CheckieDatabase
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckiePictureDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieReviewDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.CheckieTagDao
import com.perfomer.checkielite.core.data.datasource.database.room.dao.RecentSearchDao
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagReviewBoundDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.RecentSearchedReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDb
import com.perfomer.checkielite.core.data.datasource.database.room.mapper.toDomain
import com.perfomer.checkielite.core.data.util.version
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

internal interface DatabaseDataSource {

    fun getReviews(): Flow<List<CheckieReview>>

    fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>>

    fun getReview(reviewId: String): Flow<CheckieReview>

    fun getRecentSearches(): Flow<List<CheckieReview>>

    suspend fun rememberRecentSearch(
        reviewId: String,
        searchDate: Date,
    )

    suspend fun clearRecentSearches()

    suspend fun getAllBrands(): List<String>

    suspend fun createReview(
        id: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
        creationDate: Date,
        modificationDate: Date,
        isSyncing: Boolean,
    )

    suspend fun updateReview(
        id: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        deletedPictures: List<CheckiePicture>,
        actualPictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
        creationDate: Date,
        modificationDate: Date,
        isSyncing: Boolean,
    )

    suspend fun deleteReview(
        reviewId: String,
        deletedPictures: List<CheckiePicture>,
    )

    suspend fun updatePictures(pictures: List<CheckiePicture>)

    suspend fun updateSyncing(reviewId: String, isSyncing: Boolean)

    suspend fun dropSyncing()

    fun isSyncing(): Flow<Boolean>

    fun getTags(searchQuery: String, maxCount: Int): Flow<List<CheckieTag>>

    suspend fun getTag(id: String): CheckieTag

    suspend fun getTagByName(name: String): CheckieTag?

    suspend fun createTag(tag: CheckieTag)

    suspend fun updateTag(tag: CheckieTag)

    suspend fun deleteTag(tagId: String)

    suspend fun getUsedCurrencies(): List<CheckieCurrency>

    fun getDatabaseSourcePath(): String

    fun getDatabaseVersion(): Int

    suspend fun getAllPictures(): List<CheckiePicture>
}

internal class DatabaseDataSourceImpl(
    private val context: Context,
    private val database: CheckieDatabase,
) : DatabaseDataSource {

    private val reviewDao: CheckieReviewDao by lazy { database.reviewDao() }
    private val pictureDao: CheckiePictureDao by lazy { database.pictureDao() }
    private val tagDao: CheckieTagDao by lazy { database.tagDao() }
    private val recentSearchDao: RecentSearchDao by lazy { database.recentSearchDao() }

    override fun getReviews(): Flow<List<CheckieReview>> {
        return reviewDao.getReviews()
            .map { reviewsDb -> reviewsDb.map { it.toDomain(context) } }
    }

    override fun getReviewsByBrand(brand: String): Flow<List<CheckieReview>> {
        return reviewDao.getReviewsByBrand(brand)
            .map { reviewsDb -> reviewsDb.map { it.toDomain(context) } }
    }

    override fun getReview(reviewId: String): Flow<CheckieReview> {
        return reviewDao.getReview(reviewId)
            .map { it.toDomain(context) }
    }

    override fun getRecentSearches(): Flow<List<CheckieReview>> {
        return recentSearchDao.getRecentSearches()
            .map { recentSearches -> recentSearches.map { it.toDomain(context) } }
    }

    override suspend fun rememberRecentSearch(reviewId: String, searchDate: Date) = database.withTransaction {
        val recentSearch = RecentSearchedReviewDb(reviewId, searchDate)
        recentSearchDao.addRecentSearch(recentSearch)
        recentSearchDao.trimRecentSearches(leaveLatest = MAX_RECENT_SEARCHES)
    }

    override suspend fun clearRecentSearches() {
        recentSearchDao.trimRecentSearches(leaveLatest = 0)
    }

    override suspend fun getAllBrands(): List<String> {
        return reviewDao.getAllBrands()
    }

    override suspend fun createReview(
        id: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        pictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
        creationDate: Date,
        modificationDate: Date,
        isSyncing: Boolean,
    ) = database.withTransaction {
        val reviewDb = CheckieReviewDb(
            id = id,
            productName = productName,
            brandName = productBrand?.takeIf { it.isNotBlank() },
            price = price?.toDb(),
            rating = rating,
            reviewText = comment?.takeIf { it.isNotBlank() },
            advantages = advantages?.takeIf { it.isNotBlank() },
            disadvantages = disadvantages?.takeIf { it.isNotBlank() },
            creationDate = creationDate,
            modificationDate = modificationDate,
            isSyncing = isSyncing,
        )

        val tagsBoundsDb = tagsIds.map { tagId -> CheckieTagReviewBoundDb(tagId = tagId, reviewId = id) }
        val picturesDb = pictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = id, order = i, context = context)
        }

        reviewDao.insertReview(reviewDb)
        pictureDao.insertPictures(picturesDb)
        tagDao.insertBounds(tagsBoundsDb)
    }

    override suspend fun updateReview(
        id: String,
        productName: String,
        productBrand: String?,
        price: CheckiePrice?,
        rating: Int,
        deletedPictures: List<CheckiePicture>,
        actualPictures: List<CheckiePicture>,
        tagsIds: Set<String>,
        comment: String?,
        advantages: String?,
        disadvantages: String?,
        creationDate: Date,
        modificationDate: Date,
        isSyncing: Boolean
    ) = database.withTransaction {
        val reviewDb = CheckieReviewDb(
            id = id,
            productName = productName,
            brandName = productBrand?.takeIf { it.isNotBlank() },
            price = price?.toDb(),
            rating = rating,
            reviewText = comment?.takeIf { it.isNotBlank() },
            advantages = advantages?.takeIf { it.isNotBlank() },
            disadvantages = disadvantages?.takeIf { it.isNotBlank() },
            creationDate = creationDate,
            modificationDate = modificationDate,
            isSyncing = isSyncing,
        )

        val tagsBoundsDb = tagsIds.map { tagId -> CheckieTagReviewBoundDb(tagId = tagId, reviewId = id) }
        val deletedPicturesIds = deletedPictures.map { it.id }
        val actualPicturesDb = actualPictures.mapIndexed { i, picture ->
            picture.toDb(reviewId = id, order = i, context = context)
        }

        reviewDao.updateReview(reviewDb)

        pictureDao.insertPictures(actualPicturesDb)
        pictureDao.deletePictures(deletedPicturesIds)

        tagDao.deleteBoundsForReview(id)
        tagDao.insertBounds(tagsBoundsDb)
    }

    override suspend fun deleteReview(reviewId: String, deletedPictures: List<CheckiePicture>) = database.withTransaction {
        val deletedPicturesIds = deletedPictures.map { it.id }

        pictureDao.deletePictures(deletedPicturesIds)
        reviewDao.deleteReview(reviewId)
    }

    override suspend fun updatePictures(pictures: List<CheckiePicture>) = database.withTransaction {
        pictures.forEach { picture ->
            pictureDao.updatePictureUri(
                pictureId = picture.id,
                pictureUri = picture.uri,
                pictureSource = picture.source.name,
            )
        }
    }

    override suspend fun updateSyncing(reviewId: String, isSyncing: Boolean) {
        reviewDao.updateSyncing(reviewId, isSyncing)
    }

    override suspend fun dropSyncing() {
        reviewDao.dropSyncing()
    }

    override fun isSyncing(): Flow<Boolean> {
        return reviewDao.getSyncingCount().map { it > 0 }
    }

    override fun getTags(searchQuery: String, maxCount: Int): Flow<List<CheckieTag>> {
        val tags = if (searchQuery.isBlank()) {
            tagDao.getTags(maxCount = maxCount)
        } else {
            tagDao.getTagsByQuery(searchQuery, maxCount)
        }

        return tags.map { tagsDb -> tagsDb.map { it.toDomain() } }
    }

    override suspend fun getTag(id: String): CheckieTag {
        return tagDao.getTag(id).toDomain()
    }

    override suspend fun getTagByName(name: String): CheckieTag? {
        return tagDao.getTagByName(name)?.toDomain()
    }

    override suspend fun createTag(tag: CheckieTag) {
        tagDao.insertTag(tag.toDb())
    }

    override suspend fun updateTag(tag: CheckieTag) {
        tagDao.updateTag(id = tag.id, value = tag.value, emoji = tag.emoji)
    }

    override suspend fun deleteTag(tagId: String) {
        tagDao.deleteTag(tagId)
    }

    override suspend fun getUsedCurrencies(): List<CheckieCurrency> {
        return reviewDao.getUsedCurrencies()
            .map(::CheckieCurrency)
    }

    override fun getDatabaseSourcePath(): String {
        return requireNotNull(database.openHelper.writableDatabase.path) { "Database path is null" }
    }

    override fun getDatabaseVersion(): Int {
        return database.version
    }

    override suspend fun getAllPictures(): List<CheckiePicture> {
        return pictureDao.getAllPictures().map { it.toDomain(context) }
    }

    private companion object {
        private const val MAX_RECENT_SEARCHES = 10
    }
}