package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag

internal data class TagsState(
    val suggestedTags: Lce<List<CheckieTag>> = Lce.initial(),
    val selectedTagsIds: List<String> = emptyList(),
    val searchQuery: String = "",
)