package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDetailsDestination(
    val reviewId: String,
) : Destination()