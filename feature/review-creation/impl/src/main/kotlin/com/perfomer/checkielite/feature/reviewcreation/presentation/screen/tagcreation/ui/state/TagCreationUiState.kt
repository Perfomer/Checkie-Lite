package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEmojiCategory
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class TagCreationUiState(
    val title: String,
    val tagValue: String,
    val tagValueError: String?,
    val isInteractive: Boolean,
    val isDeleteAvailable: Boolean,
    val selectedEmoji: String?,
    val emojis: ImmutableList<TagCreationEmojiCategory>,
)
