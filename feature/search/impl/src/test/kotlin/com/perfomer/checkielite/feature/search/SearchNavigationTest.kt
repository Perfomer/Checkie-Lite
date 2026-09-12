package com.perfomer.checkielite.feature.search

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.transition.ReviewContent
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SearchNavigationTest {

    @Test
    fun `search contributes its review details transition`() {
        searchModules

        assertEquals(
            setOf(ReviewContent),
            NavigationRegistry.sharedTransitionGroups(
                SearchDestination(tagId = null),
                ReviewDetailsDestination(reviewId = "review"),
            ),
        )

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = SearchDestination(tagId = null),
                target = ReviewDetailsDestination(reviewId = "review"),
            ),
        )
    }
}
