package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
data class SearchDestination(
    val tagId: String?,
) : Destination()