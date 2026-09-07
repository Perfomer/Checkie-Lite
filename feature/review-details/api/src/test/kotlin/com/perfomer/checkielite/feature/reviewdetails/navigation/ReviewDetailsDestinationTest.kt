package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.Date

internal class ReviewDetailsDestinationTest {

    @Test
    fun `review id defines destination identity`() {
        val destination = ReviewDetailsDestination(reviewId = "review")

        assertEquals(destination, destination.copy())
        assertNotEquals(destination, destination.copy(reviewId = "other"))
    }

    @Test
    fun `destination survives serialization`() {
        val destination = ReviewDetailsDestination(reviewId = "review")
        val restored = Json.decodeFromString(
            ReviewDetailsDestination.serializer(),
            Json.encodeToString(ReviewDetailsDestination.serializer(), destination),
        )

        assertEquals(destination, restored)
    }

    @Test
    fun `initial review is carried separately from the route`() {
        val review = review()
        val data = reviewDetailsNavigationData(review)

        assertSame(review, data.reviewDetailsInitialReview())
        assertNull(reviewDetailsNavigationData(initialReview = null).reviewDetailsInitialReview())
    }

    private fun review() = CheckieReview(
        id = "review",
        productName = "Product",
        productBrand = null,
        price = null,
        rating = 5,
        pictures = emptyList(),
        tags = emptyList(),
        comment = null,
        advantages = null,
        disadvantages = null,
        creationDate = Date(0L),
        modificationDate = Date(0L),
        isSyncing = false,
    )
}
