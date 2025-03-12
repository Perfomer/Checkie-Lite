package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import kotlinx.collections.immutable.toPersistentList

internal class MainUiStateMapper : UiStateMapper<MainState, MainUiState> {

    override fun map(state: MainState): MainUiState {
        return when (state.reviews) {
            is Lce.Content -> {
                createContent(
                    reviews = state.reviews.requireContent(),
                    tags = state.tags.content.orEmpty(),
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
        tags: List<CheckieTag>,
    ): MainUiState {
        return if (reviews.isEmpty()) {
            MainUiState.Empty
        } else {
            MainUiState.Content(
                reviews = reviews.fastMap { it.toUiItem() }.toPersistentList(),
                tags = tags.take(MAX_TAGS)
                    .fastMap { it.toUiItem() }
                    .toPersistentList(),
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
            isSyncing = isSyncing,
        )
    }

    private fun CheckieTag.toUiItem(): Tag {
        return Tag(
            id = id,
            value = value,
            emoji = emoji,
        )
    }

    private companion object {
        private const val MAX_TAGS: Int = 30
    }
}