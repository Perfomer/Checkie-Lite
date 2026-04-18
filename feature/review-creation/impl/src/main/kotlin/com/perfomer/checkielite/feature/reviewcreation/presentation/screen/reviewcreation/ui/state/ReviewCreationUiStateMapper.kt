package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.price.CurrencySymbol
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import kotlinx.collections.immutable.toPersistentList

internal class ReviewCreationUiStateMapper : UiStateMapper<ReviewCreationState, ReviewCreationUiState> {

    override fun map(state: ReviewCreationState): ReviewCreationUiState {
        val pages = ReviewCreationPage.entries
        val currentStep = pages.indexOf(state.currentPage)
        val stepsCount = pages.size

        return ReviewCreationUiState(
            step = currentStep,
            stepsCount = stepsCount,
            currentPage = state.currentPage,
            productInfoState = createProductInfoPageState(state),
            tagsState = createTagsPageState(state),
            reviewInfoState = createReviewInfoPageState(state),
            isPrimaryButtonLoading = state.isSavingInProgress,
            isManualBackHandlerEnabled = shouldEnableManualBackHandler(state),
        )
    }

    private fun shouldEnableManualBackHandler(state: ReviewCreationState): Boolean {
        // If there are pages behind, we should handle back manually: go to the previous page.
        val hasPagesBehind = state.currentPage != ReviewCreationPage.entries.first()
        // If there is something changed, we should handle back manually: show confirmation dialog.
        val hasSomethingChanged = state.reviewDetails != state.initialReviewDetails
        // If saving is in progress, we should handle back manually: block exiting.
        val isSavingInProgress = state.isSavingInProgress

        return hasPagesBehind || hasSomethingChanged || isSavingInProgress
    }

    private fun createProductInfoPageState(state: ReviewCreationState): ProductInfoPageUiState {
        val priceCurrency = state.currentPriceCurrency

        return ProductInfoPageUiState(
            productName = state.reviewDetails.productName,
            brand = state.reviewDetails.productBrand,
            brandSuggestions = state.suggestedBrands.toPersistentList(),
            picturesUri = state.reviewDetails.pictures
                .map { ProductInfoPageUiState.Picture(id = it.id, uri = it.uri) }
                .toPersistentList(),
            price = state.currentPriceFieldValue,
            priceCurrency = Text.raw(CurrencySymbol.getSymbol(priceCurrency.code) ?: priceCurrency.symbol),
            productNameErrorText = Text.resource(R.string.reviewcreation_productinfo_field_product_error_empty)
                .takeUnless { state.isProductNameValid },
        )
    }

    private fun createTagsPageState(state: ReviewCreationState): TagsPageUiState {
        val recommendedTagIds = state.recommendedTags
            .takeIf { state.tagsSearchQuery.isBlank() }
            .orEmpty()
            .map { it.id }

        val tags = state.tags.fastMap { tag ->
            tag.toUi(
                isSelected = state.reviewDetails.tagsIds.any { tagId -> tagId == tag.id },
                isRecommended = tag.id in recommendedTagIds,
            )
        }

        val selectedTagsSelectionOrder = state.selectedTagsSelectionOrder.filter(state.reviewDetails.tagsIds::contains)
        val tagsBySelectionOrder = selectedTagsSelectionOrder.withIndex().associate { (index, tagId) -> tagId to index }
        val sortedTags = tags.sortedWith(
            compareBy<TagsPageUiState.Tag> {
                when {
                    it.isSelected -> 0
                    it.isRecommended -> 1
                    else -> 2
                }
            }.thenBy { tag ->
                if (tag.isSelected) {
                    tagsBySelectionOrder[tag.id] ?: Int.MAX_VALUE
                } else {
                    0
                }
            }
        )

        return TagsPageUiState(
            mainPictureUri = state.reviewDetails.pictures.firstOrNull()?.uri,
            productName = Text.raw(state.reviewDetails.productName),
            searchQuery = state.tagsSearchQuery,
            shouldShowAddTag = sortedTags.fastAll { it.value != Text.raw(state.tagsSearchQuery) },
            tags = sortedTags.toPersistentList(),
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            mainPictureUri = state.reviewDetails.pictures.firstOrNull()?.uri,
            productName = Text.raw(state.reviewDetails.productName),
            rating = state.reviewDetails.rating,
            comment = state.reviewDetails.comment,
            advantages = state.reviewDetails.advantages,
            disadvantages = state.reviewDetails.disadvantages,
            isSaving = state.isSavingInProgress,
        )
    }

    private companion object {

        private fun CheckieTag.toUi(
            isSelected: Boolean = false,
            isRecommended: Boolean = false,
        ): TagsPageUiState.Tag {
            return TagsPageUiState.Tag(
                id = id,
                value = Text.raw(value),
                emoji = emoji,
                isSelected = isSelected,
                isRecommended = isRecommended,
            )
        }
    }
}
