package com.perfomer.checkielite.feature.appfunctions.entity

import androidx.appfunctions.AppFunctionSerializable

/**
 * A tracked product review returned to the assistant.
 */
@AppFunctionSerializable(isDescribedByKDoc = true)
internal data class TrackedReview(
    /** Unique review id. */
    val id: String,
    /** Product name. */
    val productName: String,
    /** Brand, if any. */
    val productBrand: String?,
    /** Score from 0 to 10. */
    val rating: Int,
    /** Optional comment. */
    val comment: String?,
)
