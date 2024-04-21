package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState

internal class TagCreationUiStateMapper(
    private val context: Context,
) : UiStateMapper<TagCreationState, TagCreationUiState> {

    override fun map(state: TagCreationState): TagCreationUiState {
        return TagCreationUiState(
            title = when (state.mode) {
                is TagCreationMode.Creation -> context.getString(R.string.tagcreation_creation_title)
                is TagCreationMode.Modification -> context.getString(R.string.tagcreation_modification_title)
            },
            tagValue = state.tagValue,
            isInteractive = !state.isBusy,
            isDeleteAvailable = state.mode is TagCreationMode.Modification,
            selectedEmoji = state.selectedEmoji.takeIf { state.hasEmoji },
            emojis = state.emojis,
        )
    }
}