package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state

import androidx.compose.runtime.Immutable
import com.chrynan.emoji.core.EmojiCategory
import kotlinx.collections.immutable.PersistentList

@Immutable
internal data class TagCreationUiState(
    val isInteractive: Boolean,
    val isDeleteAvailable: Boolean,
    val selectedEmoji: String?,
    val emojis: PersistentList<EmojiCategory>,
)