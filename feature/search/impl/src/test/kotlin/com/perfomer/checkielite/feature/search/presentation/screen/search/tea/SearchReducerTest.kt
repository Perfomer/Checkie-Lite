package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.Date

internal class SearchReducerTest {

    @Test
    fun `opening a visible review passes its snapshot`() {
        val review = review(id = "selected")
        val update = SearchReducer().reduce(
            currentState = SearchState(recentSearches = Lce.Content(listOf(review))),
            event = SearchUiEvent.OnReviewClick(reviewId = review.id),
        )
        val command = update.commands.filterIsInstance<OpenReviewDetails>().single()

        assertEquals(review.id, command.reviewId)
        assertSame(review, command.initialReview)
    }

    @Test
    fun `opening a missing review still navigates without a snapshot`() {
        val update = SearchReducer().reduce(
            currentState = SearchState(),
            event = SearchUiEvent.OnReviewClick(reviewId = "selected"),
        )
        val command = update.commands.filterIsInstance<OpenReviewDetails>().single()

        assertEquals("selected", command.reviewId)
        assertNull(command.initialReview)
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
