package com.perfomer.checkielite.feature.appfunctions.mapper

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.feature.appfunctions.entity.TrackedReview

internal fun CheckieReview.toTrackedReview(): TrackedReview = TrackedReview(
    id = id,
    productName = productName,
    productBrand = productBrand,
    rating = rating,
    comment = comment,
)
