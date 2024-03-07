package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.entity.CheckiePicture

internal fun CheckiePicture.toDb(
    reviewId: String,
    order: Int,
): CheckieReviewPictureDb {
    return CheckieReviewPictureDb(
        id = id,
        order = order,
        reviewId = reviewId,
        uri = uri,
    )
}

internal fun CheckieReviewPictureDb.toDomain(): CheckiePicture {
    return CheckiePicture(
        id = id,
        uri = uri,
    )
}