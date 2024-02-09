package com.perfomer.checkielite.core.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.perfomer.checkielite.core.data.datasource.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.room.entity.relation.CheckieReviewWithPictures

@Dao
internal interface CheckieReviewDao {

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb ORDER BY creationDate DESC")
    suspend fun getReviews(): List<CheckieReviewWithPictures>

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
    suspend fun getReviewsByQuery(query: String): List<CheckieReviewWithPictures>

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb WHERE id = :id")
    suspend fun getReview(id: String): CheckieReviewWithPictures

    @Insert
    suspend fun insertReview(review: CheckieReviewDb)

    @Insert
    suspend fun insertPictures(pictures: List<CheckieReviewPictureDb>)
}