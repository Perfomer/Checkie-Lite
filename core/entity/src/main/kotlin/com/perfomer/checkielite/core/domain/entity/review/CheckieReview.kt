package com.perfomer.checkielite.core.domain.entity.review

import com.perfomer.checkielite.core.domain.entity.price.CheckiePrice
import java.util.Date

data class CheckieReview(
    val id: String,
    val productName: String,
    val productBrand: String?,
    val price: CheckiePrice?,
    val rating: Int,
    val pictures: List<CheckiePicture>,
    val tags: List<CheckieTag>,
    val comment: String?,
    val advantages: String?,
    val disadvantages: String?,
    val creationDate: Date,
    val modificationDate: Date,
    val isSyncing: Boolean,
)