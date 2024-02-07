package com.perfomer.checkielite.core.entity

import java.util.Date

data class CheckieReview(
    val id: String,
    val productName: String,
    val productBrand: String?,
    val rating: Int,
    val picturesUri: List<String>,
    val reviewText: String?,
    val creationDate: Date,
)