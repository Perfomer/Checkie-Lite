package com.perfomer.checkielite.navigation

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class NavigationTransitionsTest {

    @Test
    fun `review details can share content with every review source`() {
        val details = ReviewDetailsDestination(reviewId = "review")

        assertTrue(NavigationRegistry.hasSharedTransition(MainDestination, details))
        assertTrue(NavigationRegistry.hasSharedTransition(SearchDestination(tagId = null), details))
        assertTrue(NavigationRegistry.hasSharedTransition(details, details.copy(reviewId = "other")))
    }

    @Test
    fun `unregistered direction keeps the regular screen transition`() {
        assertFalse(
            NavigationRegistry.hasSharedTransition(
                source = ReviewDetailsDestination(reviewId = "review"),
                target = SearchDestination(tagId = null),
            ),
        )
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun registerTransitions() {
            registerNavigationTransitions()
        }
    }
}
