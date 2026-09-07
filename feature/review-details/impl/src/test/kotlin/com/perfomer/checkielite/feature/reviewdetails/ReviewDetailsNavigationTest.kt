package com.perfomer.checkielite.feature.reviewdetails

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReviewDetailsNavigationTest {

    @Test
    fun `review details contributes its nested transition`() {
        reviewDetailsModules

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = ReviewDetailsDestination(reviewId = "source"),
                target = ReviewDetailsDestination(reviewId = "target"),
            ),
        )
    }
}
