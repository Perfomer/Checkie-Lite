package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return MainUiState(
            searchQuery = state.searchQuery,
            reviews = state.reviews.map { it.toUiItem() },
        )
    }

    private fun CheckieReview.toUiItem(): ReviewItem {
        return ReviewItem(
            id = id,
            title = productName,
            brand = productBrand,
            imageUri = picturesUri.firstOrNull(),
            rating = rating,
            emoji = defineRatingEmoji(rating),
        )
    }

    private fun defineRatingEmoji(rating: Int): String = when (rating) {
        0 -> "💩"
        1, 2, 3 -> "😭"
        4, 5, 6 -> "😐"
        7, 8, 9 -> "😍"
        10 -> "💎"
        else -> throw IllegalArgumentException("Rating must be between 0 and 10")
    }
}