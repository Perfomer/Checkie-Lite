package com.perfomer.checkielite.core.data.datasource.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal class CheckieTagDb(
    @PrimaryKey val id: String,
    val value: String,
    val emoji: String?,
)