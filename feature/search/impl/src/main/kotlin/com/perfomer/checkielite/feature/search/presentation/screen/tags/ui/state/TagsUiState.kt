package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.ui.util.resource.text.Text

@Immutable
internal data class TagsUiState(
    val tags: List<Tag>,
    val searchQuery: String,
) {

    @Immutable
    data class Tag(
        val tagId: String,
        val text: Text,
        val emoji: String?,
        val isSelected: Boolean,
    )
}