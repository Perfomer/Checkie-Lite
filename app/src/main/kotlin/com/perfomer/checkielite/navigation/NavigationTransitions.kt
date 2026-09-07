package com.perfomer.checkielite.navigation

import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.core.navigation.sharedTransition
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination

internal fun registerNavigationTransitions() {
    navigation {
        sharedTransition<MainDestination, ReviewDetailsDestination>()
        sharedTransition<SearchDestination, ReviewDetailsDestination>()
        sharedTransition<ReviewDetailsDestination, ReviewDetailsDestination>()
    }
}
