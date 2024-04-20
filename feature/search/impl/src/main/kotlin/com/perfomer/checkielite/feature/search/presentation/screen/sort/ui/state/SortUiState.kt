package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.core.data.datasource.sort.ReviewsSortingStrategy

@Immutable
internal data class SortUiState(
    val items: List<SortingOption>,
    val isAscending: Boolean,
)

@Immutable
internal data class SortingOption(
    val type: ReviewsSortingStrategy,
    val text: String,
    @DrawableRes val icon: Int,
    val isSelected: Boolean,
)