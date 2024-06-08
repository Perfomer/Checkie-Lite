package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state

import android.content.Context
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsState

internal class TagsUiStateMapper(
    private val context: Context,
) : UiStateMapper<TagsState, TagsUiState> {

    override fun map(state: TagsState): TagsUiState {
        val tags = state.suggestedTags.content.orEmpty()

        return TagsUiState(
            tags = tags.map { tag ->
                TagsUiState.Tag(
                    tagId = tag.id,
                    text = tag.value,
                    emoji = tag.emoji,
                    isSelected = state.selectedTags.any { it.id == tag.id },
                )
            },
            searchQuery = state.searchQuery,
        )
    }
}