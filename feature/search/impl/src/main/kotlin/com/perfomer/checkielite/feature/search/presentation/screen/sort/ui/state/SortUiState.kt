package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy

@Immutable
internal data class SortUiState(
    val items: List<SortingOption>,
)

@Immutable
internal data class SortingOption(
    val type: ReviewsSortingStrategy,
    val text: String,
    val isSelected: Boolean,
)