package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CheckieReviewDb::class,
            parentColumns = ["id"],
            childColumns = ["reviewId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
internal data class RecentSearchedReviewDb(
    @PrimaryKey val reviewId: String,
    val searchDate: Date,
)