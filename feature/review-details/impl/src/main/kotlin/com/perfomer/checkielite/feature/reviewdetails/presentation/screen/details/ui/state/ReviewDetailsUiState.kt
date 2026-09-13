package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed interface ReviewDetailsUiState {

    val isMenuAvailable: Boolean

    data object Loading : ReviewDetailsUiState {
        override val isMenuAvailable: Boolean = true
    }

    data class Content(
        val reviewId: String,
        val brandName: Text?,
        val productName: Text,
        val date: Text,
        val rating: Int,
        val price: Price?,
        val picturesUri: ImmutableList<String>,
        val currentPicturePosition: Int,
        val comment: Text?,
        val advantages: Text?,
        val disadvantages: Text?,
        val tags: ImmutableList<Tag>,
        val recommendations: ImmutableList<RecommendedReview>,
        override val isMenuAvailable: Boolean,
    ) : ReviewDetailsUiState

    data object Error : ReviewDetailsUiState {
        override val isMenuAvailable: Boolean = false
    }
}

@Immutable
internal data class RecommendedReview(
    val reviewId: String,
    val brandName: Text?,
    val productName: Text,
    val pictureUri: String?,
    val rating: Int,
    val isSyncing: Boolean,
)

@Immutable
internal data class Tag(
    val tagId: String,
    val text: Text,
    val emoji: String?,
)

@Immutable
internal data class Price(
    val value: Text,
    val fractionalPartIndices: IntRange?,
)
