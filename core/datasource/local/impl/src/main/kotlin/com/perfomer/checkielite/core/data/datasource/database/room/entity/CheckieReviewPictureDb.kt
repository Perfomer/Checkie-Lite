package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CheckieReviewDb::class,
            parentColumns = ["id"],
            childColumns = ["reviewId"],
        ),
    ]
)
internal data class CheckieReviewPictureDb(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val reviewId: String,
    val uri: String,
)