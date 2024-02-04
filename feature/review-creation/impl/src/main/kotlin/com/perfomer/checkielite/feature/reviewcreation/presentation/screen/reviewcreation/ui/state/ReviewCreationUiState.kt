package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage

@Immutable
internal data class ReviewCreationUiState(
    val step: Int,
    val stepsCount: Int,
    val currentPage: ReviewCreationPage,
    val primaryButtonText: String,
)