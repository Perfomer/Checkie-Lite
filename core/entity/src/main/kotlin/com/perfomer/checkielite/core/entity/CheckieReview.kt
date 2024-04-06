package com.perfomer.checkielite.core.entity

import java.util.Date

data class CheckieReview(
    val id: String,
    val productName: String,
    val productBrand: String?,
    val rating: Int,
    val pictures: List<CheckiePicture>,
    val comment: String?,
    val advantages: String?,
    val disadvantages: String?,
    val creationDate: Date,
    val modificationDate: Date,
    val isSyncing: Boolean,
)