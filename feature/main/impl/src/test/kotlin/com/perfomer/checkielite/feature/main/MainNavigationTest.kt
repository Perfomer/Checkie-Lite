package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.transition.SharedContentMatch
import com.perfomer.checkielite.feature.main.presentation.navigation.MainDestination
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.MainContentScreen
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent.ReviewListItem
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent.ReviewPage
import com.perfomer.checkielite.feature.reviewdetails.presentation.navigation.ReviewDetailsDestination.ReviewContent.ReviewRecommendation
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination.SearchFieldContent
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MainNavigationTest {

    @Test
    fun `return from details matches its page but excludes recommendations`() {
        mainModules
        // Back keeps the original edge: main is source, details is target.
        val policy = NavigationRegistry.sharedTransitionPolicy(MainDestination, ReviewDetailsDestination("review"))

        assertNull(policy.match(ReviewRecommendation, isSource = false))
        assertEquals(SharedContentMatch(ReviewListItem, ReviewPage), policy.match(ReviewPage, isSource = false))
        assertEquals(policy.match(ReviewListItem, isSource = true), policy.match(ReviewPage, isSource = false))
    }

    @Test
    fun `association registers its screen and serializes its destination`() {
        mainModules

        assertEquals(MainContentScreen::class, NavigationRegistry.obtain(MainDestination::class))
        val serializer = NavigationRegistry.serializer()
        val encoded = Json.encodeToString(serializer, MainDestination)
        assertEquals(MainDestination, Json.decodeFromString(serializer, encoded))
    }

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
            setOf(SharedContentMatch(ReviewListItem, ReviewPage)),
            NavigationRegistry.sharedTransitionPolicy(MainDestination, ReviewDetailsDestination("review")).matches,
        )

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
