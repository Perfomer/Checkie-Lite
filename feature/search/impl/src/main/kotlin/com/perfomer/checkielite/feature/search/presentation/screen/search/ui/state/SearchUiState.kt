package com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.cui.widget.cell.ReviewItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class SearchUiState(
    val searchQuery: String,
    val ratingFilter: RatingFilter?,
    val tagFilters: ImmutableList<TagFilter>,
    val reviews: ImmutableList<ReviewItem>,
    val showRecentSearchesTitle: Boolean,
)

@Immutable
internal data class RatingFilter(
    val minRating: Int,
    val maxRating: Int,
)

@Immutable
internal data class TagFilter(
    val tagId: String,
    val emoji: String?,
    val value: String,
)