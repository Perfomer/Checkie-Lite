package com.perfomer.checkielite.core.data.datasource.database.room.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb

internal data class CheckieReviewWithPictures(
    @Embedded val review: CheckieReviewDb,

    @Relation(parentColumn = "id", entityColumn = "reviewId")
    val picturesUri: List<CheckieReviewPictureDb>,
)