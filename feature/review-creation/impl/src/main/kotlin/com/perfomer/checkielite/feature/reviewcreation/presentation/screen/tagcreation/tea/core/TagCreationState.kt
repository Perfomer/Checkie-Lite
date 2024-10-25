package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.EmojiCategoryType
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import kotlinx.collections.immutable.PersistentList

internal data class TagCreationState(
    val mode: TagCreationMode,
    val emojis: PersistentList<TagCreationEmojiCategory> = emptyPersistentList(),

    val tagValue: String = "",
    val tagInvalidReason: TagInvalidReason? = null,
    val selectedEmoji: String? = null,
    val hasEmoji: Boolean = false,

    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
) {
    val isBusy: Boolean = isDeleting || isSaving
}

internal enum class TagInvalidReason {
    NAME_IS_EMPTY,
    NAME_IS_TOO_LONG,
    NAME_CONFLICT,
}

@Immutable
internal data class TagCreationEmojiCategory(
    val type: EmojiCategoryType,
    val emojis: List<String>,
)