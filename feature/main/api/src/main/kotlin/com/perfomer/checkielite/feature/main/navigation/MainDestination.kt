package com.perfomer.checkielite.feature.main.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.SharedTransitionDestination
import kotlinx.serialization.Serializable

@Serializable
data object MainDestination : Destination(), SharedTransitionDestination {

    override val sharedTransitionGroup: String
        get() = "review"
}
