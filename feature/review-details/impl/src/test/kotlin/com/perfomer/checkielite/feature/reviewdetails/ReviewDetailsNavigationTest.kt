package com.perfomer.checkielite.feature.reviewdetails

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.transition.SharedContentMatch
import com.perfomer.checkielite.feature.gallery.presentation.navigation.GalleryDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent.ReviewPage
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent.ReviewRecommendation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReviewDetailsNavigationTest {

    @Test
    fun `review details contributes its gallery transition`() {
        reviewDetailsModules
        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = ReviewDetailsDestination("review"),
                target = GalleryDestination(listOf("photo"), 0),
            ),
        )
    }

    @Test
    fun `review details contributes its nested transition`() {
        reviewDetailsModules

        assertEquals(
            setOf(SharedContentMatch(ReviewRecommendation, ReviewPage)),
            NavigationRegistry.sharedTransitionPolicy(ReviewDetailsDestination("source"), ReviewDetailsDestination("target")).matches,
        )

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = ReviewDetailsDestination(reviewId = "source"),
                target = ReviewDetailsDestination(reviewId = "target"),
            ),
        )
    }
}
