package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect.ShowToast
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Date

internal class MainReducerTest {

    @Test
    fun `opening review passes the selected card snapshot`() {
        val review = review(id = "selected")
        val update = MainReducer().reduce(
            currentState = MainState(reviews = Lce.Content(listOf(review(id = "other"), review))),
            event = MainUiEvent.OnReviewClick(id = review.id),
        )
        val command = update.commands.single() as OpenReviewDetails

        assertEquals(review.id, command.reviewId)
        assertSame(review, command.initialReview)
    }

    @Test
    fun `opening review while list refreshes retains available snapshot`() {
        val review = review(id = "selected")
        val update = MainReducer().reduce(
            currentState = MainState(reviews = Lce.Loading(listOf(review))),
            event = MainUiEvent.OnReviewClick(id = review.id),
        )
        val command = update.commands.single() as OpenReviewDetails

        assertSame(review, command.initialReview)
    }

    @Test
    fun `opening review absent from list does not pass another card snapshot`() {
        val update = MainReducer().reduce(
            currentState = MainState(reviews = Lce.Content(listOf(review(id = "other")))),
            event = MainUiEvent.OnReviewClick(id = "selected"),
        )
        val command = update.commands.single() as OpenReviewDetails

        assertEquals("selected", command.reviewId)
        assertNull(command.initialReview)
    }

    @Test
    fun `opening review without list content still navigates by id`() {
        val update = MainReducer().reduce(
            currentState = MainState(),
            event = MainUiEvent.OnReviewClick(id = "selected"),
        )
        val command = update.commands.single() as OpenReviewDetails

        assertEquals("selected", command.reviewId)
        assertNull(command.initialReview)
    }

    @Test
    fun `created review produces success toast`() {
        val update = MainReducer().reduce(
            currentState = MainState(),
            event = MainNavigationEvent.ReviewCreated,
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.main_toast_reviewcreated), toast.text)
        assertEquals(ToastStyle.SUCCESS, toast.style)
        assertTrue(update.commands.isEmpty())
    }

    private fun review(id: String) = CheckieReview(
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
        isSyncing = false,
    )
}
