package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState

@Composable
internal fun TagCreationScreen(
    state: TagCreationUiState,
    onSelectedEmojiClick: () -> Unit = {},
    onTagValueInput: (value: String) -> Unit = {},
    onEmojiSelect: (emoji: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDeleteTagClick: () -> Unit = {},
) {
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    TagCreationScreen(state = mockUiState)
}

internal val mockUiState = TagCreationUiState(
    isInteractive = true,
    isDeleteAvailable = true,
    selectedEmoji = "\uD83D\uDC80", // 💀
    emojis = emptyPersistentList(),
)