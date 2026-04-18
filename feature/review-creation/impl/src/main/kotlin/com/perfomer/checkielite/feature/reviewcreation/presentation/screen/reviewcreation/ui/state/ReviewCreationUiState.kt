package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class ReviewCreationUiState(
    val step: Int,
    val stepsCount: Int,
    val currentPage: ReviewCreationPage,

    val productInfoState: ProductInfoPageUiState,
    val tagsState: TagsPageUiState,
    val reviewInfoState: ReviewInfoPageUiState,

    val isPrimaryButtonLoading: Boolean,
    val isManualBackHandlerEnabled: Boolean,
)

@Immutable
internal data class ProductInfoPageUiState(
    val productName: String,
    val productNameErrorText: Text?,
    val brand: String,
    val brandSuggestions: ImmutableList<String>,
    val price: String,
    val priceCurrency: Text,
    val picturesUri: ImmutableList<Picture>,
) {

    @Immutable
    data class Picture(
        val id: String,
        val uri: String,
    )
}

@Immutable
internal data class TagsPageUiState(
    val mainPictureUri: String?,
    val productName: Text,
    val searchQuery: String,
    val shouldShowAddTag: Boolean,
    val tags: ImmutableList<Tag>,
) {
    @Immutable
    data class Tag(
        val id: String,
        val value: Text,
        val emoji: String?,
        val isSelected: Boolean,
        val isRecommended: Boolean,
    )
}

@Immutable
internal data class ReviewInfoPageUiState(
    val mainPictureUri: String?,
    val productName: Text,
    val rating: Int,
    val comment: String,
    val advantages: String,
    val disadvantages: String,
    val isSaving: Boolean,
)
