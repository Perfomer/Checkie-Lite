package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy

internal data class SortState(
    val currentOption: ReviewsSortingStrategy,
    val sortingOptions: List<ReviewsSortingStrategy> = ReviewsSortingStrategy.entries,
)