package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.perfomer.checkielite.core.data.datasource.database.room.entity.RecentSearchedReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.relation.CheckieReviewDetailedDb
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RecentSearchDao {

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query(
        """
            SELECT * FROM RecentSearchedReviewDb recentSearch
            LEFT JOIN CheckieReviewDb review ON (review.id = recentSearch.reviewId)
            ORDER BY recentSearch.searchDate DESC
        """
    )
    fun getRecentSearches(): Flow<List<CheckieReviewDetailedDb>>

    @Query(
        """
        DELETE FROM RecentSearchedReviewDb 
        WHERE rowId NOT IN 
            (
            SELECT rowId FROM RecentSearchedReviewDb
            ORDER BY searchDate DESC
            LIMIT :leaveLatest
            )
        """
    )
    suspend fun trimRecentSearches(leaveLatest: Int = 10)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentSearch(recentSearch: RecentSearchedReviewDb)
}