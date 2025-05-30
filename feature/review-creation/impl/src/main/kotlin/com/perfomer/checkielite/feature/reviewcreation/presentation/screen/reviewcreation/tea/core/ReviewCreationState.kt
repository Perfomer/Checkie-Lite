package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.price.CheckieCurrency
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.util.LocalCheckieCurrency
import kotlinx.collections.immutable.PersistentList

internal data class ReviewCreationState(
    val mode: ReviewCreationMode,
    val currentPage: ReviewCreationPage = mode.startAction.targetPage,

    val initialReviewDetails: ReviewDetails = ReviewDetails(),
    val reviewDetails: ReviewDetails = initialReviewDetails,
    val isProductNameValid: Boolean = true,

    val currentPriceCurrency: CheckieCurrency = LocalCheckieCurrency.getLocalCurrency(),
    val currentPriceFieldValue: String = reviewDetails.price?.toString().orEmpty(),

    val tagsSearchQuery: String = "",
    val tagsSuggestions: List<CheckieTag> = emptyList(),
    val tagSorting: TagSortingStrategy = TagSortingStrategy.entries.first(),

    val suggestedBrands: List<String> = emptyList(),

    val isReviewLoading: Boolean = false,
    val isReviewLoadingFailed: Boolean = false,

    val isSavingInProgress: Boolean = false,
    val isSavingFailed: Boolean = false,
)

internal data class ReviewDetails(
    val productName: String = "",
    val productBrand: String = "",
    val price: CheckiePrice? = null,
    val pictures: PersistentList<CheckiePicture> = emptyPersistentList(),

    val tagsIds: Set<String> = emptySet(),

    val rating: Int = 5,
    val comment: String = "",
    val advantages: String = "",
    val disadvantages: String = "",
)