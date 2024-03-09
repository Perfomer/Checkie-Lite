package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.relation.CheckieReviewDetailedDb
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CheckieReviewDao {

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb ORDER BY creationDate DESC")
    fun getReviews(): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @Query(
        """
        SELECT * FROM CheckieReviewDb
        WHERE productName LIKE '%' || :query || '%'
            OR brandName LIKE '%' || :query || '%'
            OR reviewText LIKE '%' || :query || '%'
        ORDER BY creationDate DESC
        """
    )
    fun getReviewsByQuery(query: String): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb WHERE id = :id")
    fun getReview(id: String): Flow<CheckieReviewDetailedDb>

    @Insert
    suspend fun insertReview(review: CheckieReviewDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReview(review: CheckieReviewDb)

    @Query("DELETE FROM CheckieReviewDb WHERE id = :id")
    suspend fun deleteReview(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictures(pictures: List<CheckieReviewPictureDb>)

    @Query(
        """
            UPDATE CheckieReviewPictureDb
            SET uri = :pictureUri
            WHERE id = :pictureId
        """
    )
    suspend fun updatePictureUri(pictureId: String, pictureUri: String)

    @Query(
        """
            UPDATE CheckieReviewDb
            SET isSyncing = :isSyncing
            WHERE id = :reviewId
        """
    )
    suspend fun updateSyncing(reviewId: String, isSyncing: Boolean)

    @Query("DELETE FROM CheckieReviewPictureDb WHERE id IN (:picturesIds)")
    suspend fun deletePictures(picturesIds: List<String>)
}