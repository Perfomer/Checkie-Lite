package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagReviewBoundDb
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CheckieTagDao {

    @Query("SELECT * FROM CheckieTagDb WHERE id = :id")
    suspend fun getTag(id: String) : CheckieTagDb

    @Query(
        """
            SELECT * FROM CheckieTagDb tag
            ORDER BY 
                (
                    SELECT COUNT(*) FROM CheckieTagReviewBoundDb
                    WHERE tagId = tag.id
                ) DESC,
                tag.value ASC
            LIMIT :maxCount
        """
    )
    fun getTags(
        maxCount: Int,
    ): Flow<List<CheckieTagDb>>

    @Query(
        """
            SELECT * FROM CheckieTagDb tag
            WHERE tag.value  LIKE '%' || :query || '%'
            ORDER BY 
                (
                    SELECT COUNT(*) FROM CheckieTagReviewBoundDb
                    WHERE tagId = tag.id
                ) DESC,
                tag.value ASC
            LIMIT :maxCount
        """
    )
    fun getTagsByQuery(
        query: String,
        maxCount: Int,
    ): Flow<List<CheckieTagDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: CheckieTagDb)

    @Query(
        """
            UPDATE CheckieTagDb
            SET value = :value, emoji = :emoji
            WHERE id = :id
        """
    )
    suspend fun updateTag(id: String, value: String, emoji: String?)

    @Query("DELETE FROM CheckieTagDb WHERE id = :tagId")
    suspend fun deleteTag(tagId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBounds(bounds: List<CheckieTagReviewBoundDb>)

    @Query(
        """
            DELETE FROM CheckieTagReviewBoundDb
            WHERE reviewId = :reviewId
        """
    )
    suspend fun deleteBoundsForReview(reviewId: String)
}