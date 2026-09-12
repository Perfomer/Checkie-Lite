package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.core.domain.entity.review.CheckiePicture
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.PictureSource
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiStateMapper
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Date

internal class ReviewDetailsInitialStateTest {

    @Test
    fun `matching snapshot provides first frame image and review identity`() {
        val review = review()
        val state = ReviewDetailsDestination(reviewId = review.id).toInitialState(review)
        val uiState = ReviewDetailsUiStateMapper().map(state) as ReviewDetailsUiState.Content

        assertSame(review, state.review.requireContent().review)
        assertEquals(review.id, state.reviewId)
        assertTrue(state.review.requireContent().recommendations.isEmpty())
        assertEquals(review.id, uiState.reviewId)
        assertEquals(review.pictures.map { it.uri }, uiState.picturesUri)
        assertEquals(0, uiState.currentPicturePosition)
    }

    @Test
    fun `destination without snapshot starts loading`() {
        val state = ReviewDetailsDestination(reviewId = "review").toInitialState(initialReview = null)

        assertEquals("review", state.reviewId)
        assertEquals(Lce.initial(), state.review)
    }

    @Test
    fun `restoring destination drops the temporary card snapshot`() {
        val review = review()
        val destination = ReviewDetailsDestination(reviewId = review.id)
        val encoded = Json.encodeToString(ReviewDetailsDestination.serializer(), destination)
        val restored = Json.decodeFromString(ReviewDetailsDestination.serializer(), encoded)

        assertEquals(ReviewDetailsDestination(reviewId = review.id), restored)
        assertEquals(
            Lce.initial(),
            restored.toInitialState(initialReview = null).review,
        )
    }

    @Test
    fun `snapshot for another review is ignored`() {
        val state = ReviewDetailsDestination(reviewId = "selected").toInitialState(review())

        assertEquals("selected", state.reviewId)
        assertEquals(Lce.initial(), state.review)
    }

    @Test
    fun `seeded destination still subscribes to the repository`() {
        val review = review()
        val state = ReviewDetailsDestination(reviewId = review.id).toInitialState(review)
        val update = ReviewDetailsReducer().reduce(
            currentState = state,
            event = ReviewDetailsEvent.Initialize,
        )
        val command = update.commands.single() as LoadReview

        assertEquals(review.id, command.reviewId)
        assertSame(state, update.state)
        assertTrue(update.effects.isEmpty())
    }

    @Test
    fun `starting repository loading keeps the shared image visible`() {
        val review = review()
        val state = ReviewDetailsDestination(reviewId = review.id).toInitialState(review)
        val update = ReviewDetailsReducer().reduce(
            currentState = state,
            event = ReviewDetailsEvent.ReviewLoading.Started,
        )

        assertEquals(state, update.state)
        assertTrue(ReviewDetailsUiStateMapper().map(requireNotNull(update.state)) is ReviewDetailsUiState.Content)
    }

    @Test
    fun `repository result replaces snapshot and includes recommendations`() {
        val review = review()
        val state = ReviewDetailsDestination(reviewId = review.id).toInitialState(review)
        val latest = ReviewDetails(
            review = review.copy(productName = "Updated product", rating = 9),
            recommendations = listOf(review.copy(id = "recommendation")),
        )
        val update = ReviewDetailsReducer().reduce(
            currentState = state,
            event = ReviewDetailsEvent.ReviewLoading.Succeed(latest),
        )

        assertSame(latest, requireNotNull(update.state).review.requireContent())
    }

    private fun review() = CheckieReview(
        id = "review",
        productName = "Product",
        productBrand = null,
        price = null,
        rating = 5,
        pictures = listOf(
            CheckiePicture(id = "first", uri = "file:///first.jpg", source = PictureSource.APP),
            CheckiePicture(id = "second", uri = "file:///second.jpg", source = PictureSource.APP),
        ),
        tags = emptyList(),
        comment = null,
        advantages = null,
        disadvantages = null,
        creationDate = Date(0L),
        modificationDate = Date(0L),
        isSyncing = false,
    )
}
