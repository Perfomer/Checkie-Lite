package com.perfomer.checkielite.core.entity

import java.time.Instant

data class CheckieReview(
    val id: String,
    val productName: String,
    val productBrand: String,
    val rating: Int,
    val imagesUri: List<String>,
    val reviewText: String?,
    val creationDate: Instant,
)