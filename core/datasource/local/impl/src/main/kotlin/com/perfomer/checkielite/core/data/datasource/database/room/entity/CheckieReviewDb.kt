package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
internal data class CheckieReviewDb(
    @PrimaryKey val id: String,
    val productName: String,
    val brandName: String?,
    val rating: Int,
    val reviewText: String?,
    val advantages: String?,
    val disadvantages: String?,
    val creationDate: Date,
    val modificationDate: Date,
    @ColumnInfo(defaultValue = "0") val isSyncing: Boolean = false,
)