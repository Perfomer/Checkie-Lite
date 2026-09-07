package com.perfomer.checkielite.feature.search.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.SharedTransitionDestination
import kotlinx.serialization.Serializable

@Serializable
data class SearchDestination(
    val tagId: String?,
) : Destination(), SharedTransitionDestination {

    override val sharedTransitionGroup: String
        get() = "search"
}
