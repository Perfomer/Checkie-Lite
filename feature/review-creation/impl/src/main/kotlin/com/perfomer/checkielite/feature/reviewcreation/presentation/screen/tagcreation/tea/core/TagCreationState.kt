package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import kotlinx.collections.immutable.PersistentList

internal data class TagCreationState(
    val mode: TagCreationMode,
    val emojis: PersistentList<Pair<CheckieEmojiCategory, PersistentList<CheckieEmoji>>> = emptyPersistentList(),

    val tagValue: String = "",
    val selectedEmoji: String? = null,
    val hasEmoji: Boolean = false,

    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
) {
    val isBusy: Boolean = isDeleting || isSaving
}