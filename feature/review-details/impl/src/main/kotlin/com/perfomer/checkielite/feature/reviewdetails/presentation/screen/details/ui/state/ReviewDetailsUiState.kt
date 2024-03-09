package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface ReviewDetailsUiState {

    val isMenuAvailable: Boolean

    data object Loading : ReviewDetailsUiState {
        override val isMenuAvailable: Boolean = false
    }

    data class Content(
        val brandName: String?,
        val productName: String,
        val date: String,
        val rating: Int,
        val picturesUri: ImmutableList<String>,
        val currentPicturePosition: Int,
        val reviewText: String?,
        override val isMenuAvailable: Boolean,
    ) : ReviewDetailsUiState

    data object Error : ReviewDetailsUiState {
        override val isMenuAvailable: Boolean = false
    }
}
