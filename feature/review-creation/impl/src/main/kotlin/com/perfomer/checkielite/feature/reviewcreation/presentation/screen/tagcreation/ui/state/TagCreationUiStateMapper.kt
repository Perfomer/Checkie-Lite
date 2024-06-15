package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state

import android.content.Context
import androidx.annotation.StringRes
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason.NAME_CONFLICT
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason.NAME_IS_EMPTY
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason.NAME_IS_TOO_LONG

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
            tagValueError = state.tagInvalidReason?.textResource?.let(context::getString),
            isInteractive = !state.isBusy,
            isDeleteAvailable = state.mode is TagCreationMode.Modification,
            selectedEmoji = state.selectedEmoji.takeIf { state.hasEmoji },
            emojis = state.emojis,
        )
    }

    private companion object {

        @get:StringRes
        val TagInvalidReason.textResource: Int
            get() = when (this) {
                NAME_IS_EMPTY -> R.string.tagcreation_tag_name_error_empty
                NAME_CONFLICT -> R.string.tagcreation_tag_name_error_exists
                NAME_IS_TOO_LONG -> R.string.tagcreation_tag_name_error_too_long
            }
    }
}