package com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface MainUiState {

    data object Loading : MainUiState

    data class Content(
        val searchQuery: String,
        val reviews: ImmutableList<ReviewItem>,
    ) : MainUiState

    data object Empty : MainUiState

    data object Error : MainUiState
}

@Immutable
internal data class ReviewItem(
    val id: String,
    val title: String,
    val brand: String?,
    val imageUri: String?,
    val rating: Int,
    val isSyncing: Boolean,
)