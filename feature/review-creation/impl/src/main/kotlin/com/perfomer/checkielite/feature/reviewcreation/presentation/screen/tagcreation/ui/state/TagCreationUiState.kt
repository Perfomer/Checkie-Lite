package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state

import androidx.compose.runtime.Immutable
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import kotlinx.collections.immutable.PersistentList

@Immutable
internal data class TagCreationUiState(
    val title: String,
    val tagValue: String,
    val isInteractive: Boolean,
    val isDeleteAvailable: Boolean,
    val selectedEmoji: String?,
    val emojis: PersistentList<Pair<CheckieEmojiCategory, PersistentList<CheckieEmoji>>>,
)