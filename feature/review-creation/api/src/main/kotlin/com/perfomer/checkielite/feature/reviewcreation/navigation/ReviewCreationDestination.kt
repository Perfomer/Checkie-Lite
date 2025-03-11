package com.perfomer.checkielite.feature.reviewcreation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.Result
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import kotlinx.serialization.Serializable

@Serializable
data class ReviewCreationDestination(
    val mode: ReviewCreationMode,
) : Destination()

sealed interface ReviewCreationResult : Result {
    data object Success : ReviewCreationResult
}