package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.core.entity.emoji.EmojiCategoryType
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import kotlinx.collections.immutable.PersistentList

internal data class TagCreationState(
    val mode: TagCreationMode,
    val emojis: PersistentList<TagCreationEmojiCategory> = emptyPersistentList(),

    val initialTagDetails: TagDetails = TagDetails(),
    val tagDetails: TagDetails = initialTagDetails,

    val tagInvalidReason: TagInvalidReason? = null,

    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val isExitConfirmed: Boolean = false,
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

internal data class TagDetails(
    val tagValue: String = "",
    val selectedEmoji: String? = null,
    val hasEmoji: Boolean = false,
) {

    val actualEmoji: String? = selectedEmoji.takeIf { hasEmoji }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagDetails

        if (tagValue != other.tagValue) return false
        if (actualEmoji != other.actualEmoji) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagValue.hashCode()
        result = 31 * result + (actualEmoji?.hashCode() ?: 0)
        return result
    }
}