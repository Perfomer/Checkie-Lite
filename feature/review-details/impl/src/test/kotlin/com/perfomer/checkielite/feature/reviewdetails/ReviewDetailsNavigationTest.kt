package com.perfomer.checkielite.feature.reviewdetails

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.gallery.navigation.GalleryDestination
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
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

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = ReviewDetailsDestination(reviewId = "source"),
                target = ReviewDetailsDestination(reviewId = "target"),
            ),
        )
    }
}
