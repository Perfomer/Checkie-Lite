package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    primaryKeys = ["tagId", "reviewId"],
    foreignKeys = [
        ForeignKey(
            entity = CheckieReviewDb::class,
            parentColumns = ["id"],
            childColumns = ["reviewId"],
            onDelete = CASCADE,
        ),
        ForeignKey(
            entity = CheckieTagDb::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = CASCADE,
        ),
    ],
    indices = [Index("tagId"), Index("reviewId")],
)
internal data class CheckieTagReviewBoundDb(
    val tagId: String,
    val reviewId: String,
)