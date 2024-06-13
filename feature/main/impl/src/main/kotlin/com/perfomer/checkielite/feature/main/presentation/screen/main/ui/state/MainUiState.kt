package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface MainUiState {

    data object Loading : MainUiState

    data class Content(
        val tags: ImmutableList<Tag>,
        val reviews: ImmutableList<ReviewItem>,
    ) : MainUiState

    data object Empty : MainUiState

    data object Error : MainUiState
}

@Immutable
data class Tag(
    val id: String,
    val value: String,
    val emoji: String?,
)