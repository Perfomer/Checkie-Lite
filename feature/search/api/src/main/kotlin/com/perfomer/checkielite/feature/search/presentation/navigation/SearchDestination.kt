package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import kotlinx.serialization.Serializable

@Serializable
data class SearchDestination(
    val tagId: String?,
) : Destination() {
    data object SearchFieldContent : SharedContentGroup
}
