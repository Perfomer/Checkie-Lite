package com.perfomer.checkielite.core.data.datasource.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb

@Dao
internal interface CheckiePictureDao {

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

    @Query("DELETE FROM CheckieReviewPictureDb WHERE id IN (:picturesIds)")
    suspend fun deletePictures(picturesIds: List<String>)
}