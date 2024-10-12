package com.perfomer.checkielite.core.data.datasource.database.room.mapper

import android.content.Context
import com.perfomer.checkielite.core.data.datasource.database.room.entity.CheckieReviewPictureDb
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.PictureSource

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
        source = source.name,
        uri = when (source) {
            PictureSource.GALLERY -> uri
            PictureSource.APP -> uri.substring(pathLength)
        },
    )
}

internal fun CheckieReviewPictureDb.toDomain(context: Context): CheckiePicture {
    val pictureSource = PictureSource.valueOf(source)

    return CheckiePicture(
        id = id,
        source = pictureSource,
        uri = when (pictureSource) {
            PictureSource.GALLERY -> uri
            PictureSource.APP -> "${context.filesDir.absolutePath}/$uri"
        },
    )
}