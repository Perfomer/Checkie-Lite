package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.entity.CheckieTag

internal data class FilterState(
    val suggestedTags: Lce<List<CheckieTag>> = Lce.initial(),
    val selectedTags: List<CheckieTag> = emptyList(),
    val minRating: Int = 0,
    val maxRating: Int = 10,
)