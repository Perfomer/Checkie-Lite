package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import kotlinx.collections.immutable.toPersistentList

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return when (state.reviews) {
            is Lce.Content -> {
                createContent(
                    reviews = state.currentReviews.content ?: state.reviews.requireContent(),
                    searchQuery = state.searchQuery,
                )
            }

            is Lce.Error -> {
                MainUiState.Error
            }

            is Lce.Loading -> {
                MainUiState.Loading
            }
        }
    }

    private fun createContent(
        reviews: List<CheckieReview>,
        searchQuery: String,
    ): MainUiState {
        return if (reviews.isEmpty() && searchQuery.isBlank()) {
            MainUiState.Empty
        } else {
            MainUiState.Content(
                searchQuery = searchQuery,
                reviews = reviews.map { it.toUiItem() }.toPersistentList(),
            )
        }
    }

    private fun CheckieReview.toUiItem(): ReviewItem {
        return ReviewItem(
            id = id,
            title = productName,
            brand = productBrand,
            imageUri = pictures.firstOrNull()?.uri,
            rating = rating,
        )
    }
}