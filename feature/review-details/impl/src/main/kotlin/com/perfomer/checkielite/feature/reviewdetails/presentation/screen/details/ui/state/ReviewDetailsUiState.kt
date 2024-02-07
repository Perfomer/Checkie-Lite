package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ReviewDetailsUiState {

    data object Loading : ReviewDetailsUiState

    data class Content(
        val brandName: String?,
        val productName: String,
        val date: String,
        val rating: Int,
        val emoji: String,
        val picturesUri: List<String>,
        val currentPicturePosition: Int,
        val reviewText: String?,
    ) : ReviewDetailsUiState

    data object Error : ReviewDetailsUiState
}
