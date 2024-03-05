package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return when (state.reviews) {
            is Lce.Content -> createContent(state)
            is Lce.Error -> MainUiState.Error
            is Lce.Loading -> MainUiState.Loading
        }
    }

    private fun createContent(state: MainState): MainUiState {
        val reviews = state.reviews.requireContent()

        return if (reviews.isEmpty() && state.searchQuery.isBlank()) {
            MainUiState.Empty
        } else {
            MainUiState.Content(
                searchQuery = state.searchQuery,
                reviews = reviews.map { it.toUiItem() },
            )
        }
    }

    private fun CheckieReview.toUiItem(): ReviewItem {
        return ReviewItem(
            id = id,
            title = productName,
            brand = productBrand,
            imageUri = picturesUri.firstOrNull(),
            rating = rating,
        )
    }
}