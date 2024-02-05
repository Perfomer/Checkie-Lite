package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import kotlinx.collections.immutable.PersistentList

internal data class ReviewCreationState(
    val currentPage: ReviewCreationPage = ReviewCreationPage.PRODUCT_INFO,

    val productName: String = "",
    val isProductNameValid: Boolean = true,
    val brand: String = "",
    val picturesUri: PersistentList<String> = emptyPersistentList(),

    val rating: Int = 5,
    val reviewText: String = "",
)