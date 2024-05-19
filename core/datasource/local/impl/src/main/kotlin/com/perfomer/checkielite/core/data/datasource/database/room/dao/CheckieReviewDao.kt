package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.relation.CheckieReviewDetailedDb
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CheckieReviewDao {

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb ORDER BY creationDate DESC")
    fun getReviews(): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @RawQuery
    fun getReviewsByQuery(query: SupportSQLiteQuery): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @Query(
        """
        SELECT * FROM CheckieReviewDb
        WHERE brandName = :brand
        ORDER BY modificationDate DESC
        """
    )
    fun getReviewsByBrand(brand: String): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb WHERE id = :id")
    fun getReview(id: String): Flow<CheckieReviewDetailedDb>

    @Query(
        """
        SELECT DISTINCT brandName FROM CheckieReviewDb
        WHERE brandName LIKE '%' || :query || '%'
            AND brandName != :query
        LIMIT 5
        """
    )
    suspend fun searchBrands(query: String): List<String>

    @Insert
    suspend fun insertReview(review: CheckieReviewDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReview(review: CheckieReviewDb)

    @Query("DELETE FROM CheckieReviewDb WHERE id = :id")
    suspend fun deleteReview(id: String)

    @Query(
        """
            UPDATE CheckieReviewDb
            SET isSyncing = :isSyncing
            WHERE id = :reviewId
        """
    )
    suspend fun updateSyncing(reviewId: String, isSyncing: Boolean)

    @Query(
        """
            UPDATE CheckieReviewDb
            SET isSyncing = 0
            WHERE isSyncing = 1
        """
    )
    suspend fun dropSyncing()
}

internal fun createFindReviewsSqliteQuery(searchQuery: String, filters: SearchFilters, sorting: SearchSorting): SimpleSQLiteQuery {
    val params = mutableListOf<Any?>()
    var shouldAnd = false
    var query =
        """
            SELECT * FROM CheckieReviewDb review
            LEFT JOIN CheckieTagReviewBoundDb tag ON (review.id = tag.reviewId)
            WHERE 
            """

    if (searchQuery.isNotBlank()) {
        query +=
            """
               (
                    review.productName LIKE '%' || ? || '%'
                    OR review.brandName LIKE '%' || ? || '%'
                    OR review.reviewText LIKE '%' || ? || '%'
               )
                """

        params.add(searchQuery)
        params.add(searchQuery)
        params.add(searchQuery)
        shouldAnd = true
    }

    if (filters.tags.isNotEmpty()) {
        if (shouldAnd) query += " AND "
        query += " tag.tagId IN(?) "

        val tagIds = filters.tags.map { it.id }
        params.add(tagIds)
        shouldAnd = true
    }

    if (filters.ratingRange != RatingRange.default) {
        if (shouldAnd) query += " AND "
        query += " (review.rating BETWEEN ? AND ?) "

        params.add(filters.ratingRange.min)
        params.add(filters.ratingRange.max)
    }

    val orderingDirection = when (sorting.order) {
        SortingOrder.ASCENDING -> " ASC "
        SortingOrder.DESCENDING -> " DESC "
    }

    val orderingField = when (sorting.strategy) {
        ReviewsSortingStrategy.CREATION_DATE -> "creationDate"
        ReviewsSortingStrategy.RATING -> "rating"
    }

    query += " ORDER BY review.$orderingField $orderingDirection"

    return SimpleSQLiteQuery(query, params.toTypedArray())
}