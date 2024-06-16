package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.database.room.entity.relation.CheckieReviewDetailedDb
import com.perfomer.checkielite.core.entity.CheckieReview

internal fun CheckieReviewDetailedDb.toDomain(): CheckieReview {
    return CheckieReview(
        id = review.id,
        productName = review.productName,
        productBrand = review.brandName,
        price = review.price?.toDomain(),
        rating = review.rating,
        pictures = picturesUri
            .sortedWith(compareBy({ it.order }, { it.id }))
            .map { it.toDomain() },
        tags = tags.map { it.toDomain() },
        comment = review.reviewText,
        advantages = review.advantages,
        disadvantages = review.disadvantages,
        creationDate = review.creationDate,
        modificationDate = review.modificationDate,
        isSyncing = review.isSyncing,
    )
}

internal fun CheckieReview.toDb(): CheckieReviewDb {
    return CheckieReviewDb(
        id = id,
        productName = productName,
        brandName = productBrand?.takeIf { it.isNotBlank() },
        price = price?.toDb(),
        rating = rating,
        reviewText = comment?.takeIf { it.isNotBlank() },
        advantages = advantages?.takeIf { it.isNotBlank() },
        disadvantages = disadvantages?.takeIf { it.isNotBlank() },
        creationDate = creationDate,
        modificationDate = modificationDate,
        isSyncing = isSyncing,
    )
}