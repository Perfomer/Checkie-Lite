package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CheckieReviewDb::class,
            parentColumns = ["id"],
            childColumns = ["reviewId"],
        ),
    ],
    indices = [Index("reviewId")],
)
internal data class CheckieReviewPictureDb(
    @PrimaryKey val id: String,
    @ColumnInfo(defaultValue = "0") val order: Int = 0,
    val reviewId: String,
    val uri: String,
)