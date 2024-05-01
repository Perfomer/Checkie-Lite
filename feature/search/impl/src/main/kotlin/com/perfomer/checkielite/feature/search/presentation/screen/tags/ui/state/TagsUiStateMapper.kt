package com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsState

internal class TagsUiStateMapper(
    private val context: Context,
) : UiStateMapper<TagsState, TagsUiState> {

    override fun map(state: TagsState): TagsUiState {
        return TagsUiState
    }
}