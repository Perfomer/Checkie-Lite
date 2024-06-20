package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagSortingStrategy

@Immutable
internal data class TagSortUiState(
    val items: List<SortingOption>,
)

@Immutable
internal data class SortingOption(
    val type: TagSortingStrategy,
    val text: String,
    val isSelected: Boolean,
)