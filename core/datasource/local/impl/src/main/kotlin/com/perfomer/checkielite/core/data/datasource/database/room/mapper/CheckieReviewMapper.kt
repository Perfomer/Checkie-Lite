package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.relation.CheckieReviewWithPictures
import com.perfomer.checkielite.core.entity.CheckieReview

internal fun CheckieReviewWithPictures.toDomain(): CheckieReview {
    return CheckieReview(
        id = review.id,
        productName = review.productName,
        productBrand = review.brandName,
        rating = review.rating,
        pictures = picturesUri
            .sortedWith(compareBy({ it.order }, { it.id }))
            .map { it.toDomain() },
        reviewText = review.reviewText,
        creationDate = review.creationDate,
        modificationDate = review.modificationDate,
    )
}

internal fun CheckieReview.toDb(): CheckieReviewDb {
    return CheckieReviewDb(
        id = id,
        productName = productName,
        brandName = productBrand?.takeIf { it.isNotBlank() },
        rating = rating,
        reviewText = reviewText?.takeIf { it.isNotBlank() },
        creationDate = creationDate,
        modificationDate = modificationDate,
    )
}