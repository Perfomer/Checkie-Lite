package com.perfomer.checkielite.feature.reviewcreation

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.gallery.navigation.GalleryDestination
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationDestination
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ReviewCreationNavigationTest {

    @Test
    fun `editor contributes its gallery transition`() {
        reviewCreationModules
        listOf(ReviewCreationMode.Creation, ReviewCreationMode.Modification("review")).forEach { mode ->
            assertTrue(
                NavigationRegistry.hasSharedTransition(
                    source = ReviewCreationDestination(mode),
                    target = GalleryDestination(listOf("photo"), 0),
                ),
            )
        }
    }
}
