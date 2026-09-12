package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.transition.ReviewContent
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import com.perfomer.checkielite.feature.search.presentation.transition.SearchFieldContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MainNavigationTest {

    @Test
    fun `main contributes its search transition`() {
        mainModules

        assertEquals(
            setOf(SearchFieldContent),
            NavigationRegistry.sharedTransitionGroups(MainDestination, SearchDestination(tagId = null)),
        )

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = MainDestination,
                target = SearchDestination(tagId = null),
            ),
        )
    }

    @Test
    fun `main contributes its review details transition`() {
        mainModules

        assertEquals(
            setOf(ReviewContent),
            NavigationRegistry.sharedTransitionGroups(MainDestination, ReviewDetailsDestination(reviewId = "review")),
        )

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = MainDestination,
                target = ReviewDetailsDestination(reviewId = "review"),
            ),
        )
    }
}
