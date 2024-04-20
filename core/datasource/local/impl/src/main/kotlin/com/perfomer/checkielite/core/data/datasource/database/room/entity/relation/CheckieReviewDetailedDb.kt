package com.perfomer.checkielite.core.data.datasource.database.room.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieTagReviewBoundDb

internal data class CheckieReviewDetailedDb(
    @Embedded val review: CheckieReviewDb,

    @Relation(parentColumn = "id", entityColumn = "reviewId")
    val picturesUri: List<CheckieReviewPictureDb>,

    @Relation(
        parentColumn = "id",
        entity = CheckieTagDb::class,
        entityColumn = "id",
        associateBy = Junction(
            value = CheckieTagReviewBoundDb::class,
            parentColumn = "reviewId",
            entityColumn = "tagId",
        )
    )
    val tags: List<CheckieTagDb>,
)