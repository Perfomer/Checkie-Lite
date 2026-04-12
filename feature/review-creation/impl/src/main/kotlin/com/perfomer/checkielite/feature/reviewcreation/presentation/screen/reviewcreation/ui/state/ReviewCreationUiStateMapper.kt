package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import android.content.Context
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.domain.entity.price.CurrencySymbol
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import kotlinx.collections.immutable.toPersistentList

internal class ReviewCreationUiStateMapper(
    private val context: Context,
) : UiStateMapper<ReviewCreationState, ReviewCreationUiState> {

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
            priceCurrency = CurrencySymbol.getSymbol(priceCurrency.code) ?: priceCurrency.symbol,
            productNameErrorText = context.getString(R.string.reviewcreation_productinfo_field_product_error_empty)
                .takeUnless { state.isProductNameValid },
        )
    }

    private fun createTagsPageState(state: ReviewCreationState): TagsPageUiState {
        val tags = state.tags.fastMap { tag ->
            tag.toUi(isSelected = state.reviewDetails.tagsIds.any { tagId -> tagId == tag.id })
        }
        val recommendedTags = state.recommendedTags
            .takeIf { state.tagsSearchQuery.isBlank() }
            .orEmpty()
            .fastMap { tag -> tag.toUi() }

        val selectedTagsSelectionOrder = state.selectedTagsSelectionOrder.filter(state.reviewDetails.tagsIds::contains)
        val tagsBySelectionOrder = selectedTagsSelectionOrder.withIndex().associate { (index, tagId) -> tagId to index }
        val sortedTags = tags.sortedWith(
            compareBy<TagsPageUiState.Tag> { !it.isSelected }
                .thenBy { tagsBySelectionOrder[it.id] ?: Int.MAX_VALUE }
        )

        return TagsPageUiState(
            mainPictureUri = state.reviewDetails.pictures.firstOrNull()?.uri,
            productName = state.reviewDetails.productName,
            hasBrand = state.reviewDetails.productBrand.isNotBlank(),
            searchQuery = state.tagsSearchQuery,
            shouldShowAddTag = sortedTags.fastAll { it.value != state.tagsSearchQuery },
            recommendedTags = recommendedTags.toPersistentList(),
            tags = sortedTags.toPersistentList(),
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            mainPictureUri = state.reviewDetails.pictures.firstOrNull()?.uri,
            productName = state.reviewDetails.productName,
            rating = state.reviewDetails.rating,
            comment = state.reviewDetails.comment,
            advantages = state.reviewDetails.advantages,
            disadvantages = state.reviewDetails.disadvantages,
            isSaving = state.isSavingInProgress,
        )
    }

    private companion object {

        private fun CheckieTag.toUi(isSelected: Boolean = false): TagsPageUiState.Tag {
            return TagsPageUiState.Tag(
                id = id,
                value = value,
                emoji = emoji,
                isSelected = isSelected,
            )
        }
    }
}
