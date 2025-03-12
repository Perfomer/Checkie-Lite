package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core

import com.perfomer.checkielite.core.domain.entity.sort.TagSortingStrategy

internal data class TagSortState(
    val currentOption: TagSortingStrategy,
    val sortingOptions: List<TagSortingStrategy> = TagSortingStrategy.entries,
)