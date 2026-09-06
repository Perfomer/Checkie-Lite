package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Date

internal class ReviewDetailsDestinationTest {

    @Test
    fun `review identity ignores missing or updated snapshots`() {
        val review = review()
        val withoutSnapshot = ReviewDetailsDestination(reviewId = review.id)
        val withSnapshot = ReviewDetailsDestination(reviewId = review.id, initialReview = review)
        val updatedSnapshot = withSnapshot.copy(initialReview = review.copy(productName = "Updated", rating = 9))

        assertEquals(withoutSnapshot, withSnapshot)
        assertEquals(withSnapshot, withoutSnapshot)
        assertEquals(withSnapshot, updatedSnapshot)
        assertEquals(withoutSnapshot.hashCode(), withSnapshot.hashCode())
        assertEquals(withSnapshot.hashCode(), updatedSnapshot.hashCode())
        assertEquals(1, setOf(withoutSnapshot, withSnapshot, updatedSnapshot).size)
        assertSame(review, withSnapshot.initialReview)
    }

    @Test
    fun `different review ids remain different destinations`() {
        val review = review()
        val destination = ReviewDetailsDestination(reviewId = review.id, initialReview = review)

        assertNotEquals(destination, destination.copy(reviewId = "other"))
        assertNotEquals(destination, null)
        assertNotEquals(destination, review.id)
    }

    @Test
    fun `serialization drops the snapshot without changing navigation identity`() {
        val review = review()
        val destination = ReviewDetailsDestination(reviewId = review.id, initialReview = review)
        val serializer = ReviewDetailsDestination.serializer()
        val restored = Json.decodeFromString(serializer, Json.encodeToString(serializer, destination))

        assertEquals(destination, restored)
        assertEquals(destination.hashCode(), restored.hashCode())
        assertNull(restored.initialReview)
        assertSame(review, destination.initialReview)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `reopening a review preserves a unique stack across process restoration`(startsWithSnapshot: Boolean) {
        val review = review()
        val initial = ReviewDetailsDestination(
            reviewId = review.id,
            initialReview = review.takeIf { startsWithSnapshot },
        )
        val reopened = ReviewDetailsDestination(
            reviewId = review.id,
            initialReview = review.takeUnless { startsWithSnapshot },
        )
        val other = ReviewDetailsDestination(reviewId = "other")
        val navigation = StackNavigation<ReviewDetailsDestination>()
        val lifecycle = LifecycleRegistry()
        val restoredLifecycle = LifecycleRegistry()
        val stateKeeper = StateKeeperDispatcher()

        try {
            lifecycle.resume()
            val stack = DefaultComponentContext(lifecycle = lifecycle, stateKeeper = stateKeeper).childStack(
                source = navigation,
                serializer = ReviewDetailsDestination.serializer(),
                initialConfiguration = initial,
                childFactory = { configuration, _ -> configuration.reviewId },
            )

            // Main opens A with a snapshot; recommendations can reopen A without it (and vice versa).
            navigation.pushToFront(other)
            navigation.pushToFront(reopened)
            assertEquals(listOf(other.reviewId, review.id), stack.value.items.map { it.configuration.reviewId })

            // Force actual serialization: StateKeeper's in-memory container is lazy.
            val savedState = Json.encodeToString(SerializableContainer.serializer(), stateKeeper.save())
            val restoredStateKeeper = StateKeeperDispatcher(
                Json.decodeFromString(SerializableContainer.serializer(), savedState),
            )
            restoredLifecycle.resume()
            val restoredStack = DefaultComponentContext(
                lifecycle = restoredLifecycle,
                stateKeeper = restoredStateKeeper,
            ).childStack(
                source = StackNavigation<ReviewDetailsDestination>(),
                serializer = ReviewDetailsDestination.serializer(),
                initialConfiguration = ReviewDetailsDestination(reviewId = "fallback"),
                childFactory = { configuration, _ -> configuration.reviewId },
            )
            val restoredConfigurations = restoredStack.value.items.map { it.configuration }

            assertEquals(listOf(other, ReviewDetailsDestination(reviewId = review.id)), restoredConfigurations)
            assertEquals(restoredConfigurations.size, restoredConfigurations.toSet().size)
            restoredConfigurations.forEach { assertNull(it.initialReview) }
        } finally {
            lifecycle.destroy()
            restoredLifecycle.destroy()
        }
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
