package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
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
        WHERE brandName = :brand
        ORDER BY modificationDate DESC
        """
    )
    fun getReviewsByBrand(brand: String): Flow<List<CheckieReviewDetailedDb>>

    @Transaction
    @Query("SELECT * FROM CheckieReviewDb WHERE id = :id")
    fun getReview(id: String): Flow<CheckieReviewDetailedDb>

    @Query("SELECT DISTINCT brandName FROM CheckieReviewDb")
    suspend fun getAllBrands(): List<String>

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