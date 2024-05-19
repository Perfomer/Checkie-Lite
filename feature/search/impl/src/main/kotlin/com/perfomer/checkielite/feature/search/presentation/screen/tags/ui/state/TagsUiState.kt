package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class TagsUiState(
    val tags: List<Tag>,
) {

    @Immutable
    data class Tag(
        val tagId: String,
        val text: String,
        val emoji: String?,
        val isSelected: Boolean,
    )
}