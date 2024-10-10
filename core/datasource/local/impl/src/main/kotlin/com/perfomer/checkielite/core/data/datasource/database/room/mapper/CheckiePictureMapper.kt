package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import android.content.Context
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.entity.CheckiePicture

internal fun CheckiePicture.toDb(
    context: Context,
    reviewId: String,
    order: Int,
): CheckieReviewPictureDb {
    val pathLength = context.filesDir.absolutePath.length + 1

    return CheckieReviewPictureDb(
        id = id,
        order = order,
        reviewId = reviewId,
        uri = uri.substring(pathLength),
    )
}

internal fun CheckieReviewPictureDb.toDomain(context: Context): CheckiePicture {
    return CheckiePicture(
        id = id,
        uri = "${context.filesDir.absolutePath}/$uri",
    )
}