package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MainNavigationTest {

    @Test
    fun `main contributes its review details transition`() {
        mainModules

        assertTrue(
            NavigationRegistry.hasSharedTransition(
                source = MainDestination,
                target = ReviewDetailsDestination(reviewId = "review"),
            ),
        )
    }
}
