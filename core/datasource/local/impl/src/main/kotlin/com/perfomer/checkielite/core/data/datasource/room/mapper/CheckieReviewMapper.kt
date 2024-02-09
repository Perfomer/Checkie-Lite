package com.perfomer.checkielite.core.data.datasource.room.mapper

import com.perfomer.checkielite.core.data.datasource.room.entity.CheckieReviewDb
import com.perfomer.checkielite.core.data.datasource.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.data.datasource.room.entity.relation.CheckieReviewWithPictures
import com.perfomer.checkielite.core.entity.CheckieReview

internal fun CheckieReviewWithPictures.toDomain(): CheckieReview {
    return CheckieReview(
        id = review.id,
        productName = review.productName,
        productBrand = review.brandName,
        rating = review.rating,
        picturesUri = picturesUri.map { it.uri },
        reviewText = review.reviewText,
        creationDate = review.creationDate,
    )
}

internal fun CheckieReview.toDb(): CheckieReviewWithPictures {
    return CheckieReviewWithPictures(
        review = CheckieReviewDb(
            id = id,
            productName = productName,
            brandName = productBrand,
            rating = rating,
            reviewText = reviewText,
            creationDate = creationDate,
        ),
        picturesUri = picturesUri.map { uri ->
            CheckieReviewPictureDb(
                reviewId = id,
                uri = uri,
            )
        }
    )
}