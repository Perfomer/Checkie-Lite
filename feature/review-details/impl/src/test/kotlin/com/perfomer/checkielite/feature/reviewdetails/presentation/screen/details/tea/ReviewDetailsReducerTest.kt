package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowToast
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Date

internal class ReviewDetailsReducerTest {

    @Test
    fun `opening a recommendation passes its snapshot`() {
        val review = review(id = "review")
        val recommendation = review(id = "recommendation")
        val update = ReviewDetailsReducer().reduce(
            currentState = ReviewDetailsState(
                reviewId = review.id,
                review = Lce.Content(
                    ReviewDetails(
                        review = review,
                        recommendations = listOf(recommendation),
                    ),
                ),
            ),
            event = ReviewDetailsUiEvent.OnRecommendationClick(recommendation.id),
        )
        val command = update.commands.single() as OpenReviewDetails

        assertEquals(recommendation.id, command.reviewId)
        assertSame(recommendation, command.initialReview)
    }

    @Test
    fun `deleted review produces neutral toast and exits`() {
        val update = ReviewDetailsReducer().reduce(
            currentState = ReviewDetailsState(reviewId = "review"),
            event = ReviewDetailsEvent.ReviewDeletion.Succeed,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.reviewdetails_toast_deleted), toast.text)
        assertEquals(ToastStyle.NEUTRAL, toast.style)
        assertEquals(listOf(ReviewDetailsNavigationCommand.Exit), update.commands)
    }

    @Test
    fun `editing syncing review produces warning without navigation`() {
        val review = review(id = "review", isSyncing = true)
        val update = ReviewDetailsReducer().reduce(
            currentState = ReviewDetailsState(
                reviewId = review.id,
                review = Lce.Content(ReviewDetails(review = review, recommendations = emptyList())),
            ),
            event = ReviewDetailsUiEvent.OnEditClick,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(CommonString.common_toast_syncing), toast.text)
        assertEquals(ToastStyle.WARNING, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    private fun review(
        id: String,
        isSyncing: Boolean = false,
    ) = CheckieReview(
        id = id,
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
        isSyncing = isSyncing,
    )
}
