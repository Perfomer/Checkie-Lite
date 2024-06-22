package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import android.content.Context
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastMap
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.price.CurrencySymbol
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
        )
    }

    private fun createProductInfoPageState(state: ReviewCreationState): ProductInfoPageUiState {
        val priceCurrency = state.currentPriceCurrency

        return ProductInfoPageUiState(
            productName = state.reviewDetails.productName,
            brand = state.reviewDetails.productBrand,
            brandSuggestions = state.suggestedBrands.toPersistentList(),
            picturesUri = state.reviewDetails.pictures.map { it.uri }.toPersistentList(),
            price = state.currentPriceFieldValue,
            priceCurrency = CurrencySymbol.getSymbol(priceCurrency.code) ?: priceCurrency.symbol,
            productNameErrorText = context.getString(R.string.reviewcreation_productinfo_field_product_error_empty)
                .takeUnless { state.isProductNameValid },
        )
    }

    private fun createTagsPageState(state: ReviewCreationState): TagsPageUiState {
        val uiTags = state.tagsSuggestions.fastMap { tag ->
            TagsPageUiState.Tag(
                id = tag.id,
                value = tag.value,
                emoji = tag.emoji,
                isSelected = state.reviewDetails.tagsIds.any { tagId -> tagId == tag.id },
            )
        }

        return TagsPageUiState(
            searchQuery = state.tagsSearchQuery,
            shouldShowAddTag = uiTags.fastAll { it.value != state.tagsSearchQuery },
            tags = uiTags.toPersistentList(),
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            rating = state.reviewDetails.rating,
            comment = state.reviewDetails.comment,
            advantages = state.reviewDetails.advantages,
            disadvantages = state.reviewDetails.disadvantages,
            isSaving = state.isSavingInProgress,
        )
    }
}