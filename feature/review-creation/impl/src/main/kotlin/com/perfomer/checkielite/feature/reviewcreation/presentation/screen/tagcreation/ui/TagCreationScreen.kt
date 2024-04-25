package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiOutlineButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState

@Composable
internal fun TagCreationScreen(
    state: TagCreationUiState,
    focusRequester: FocusRequester,
    onSelectedEmojiClick: () -> Unit = {},
    onTagValueInput: (value: String) -> Unit = {},
    onEmojiSelect: (emoji: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDeleteTagClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.85F)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column {
            val lazyGridState = rememberLazyGridState()
            val shouldShowDivider by remember { derivedStateOf { lazyGridState.canScrollBackward } }

            Header(
                title = state.title,
                tagValue = state.tagValue,
                tagValueError = state.tagValueError,
                selectedEmoji = state.selectedEmoji,
                isDeleteButtonAvailable = state.isDeleteAvailable,
                focusRequester = focusRequester,
                onDeleteTagClick = onDeleteTagClick,
                onSelectedEmojiClick = onSelectedEmojiClick,
                onTagValueInput = onTagValueInput,
                modifier = Modifier.bottomStrokeOnScroll(
                    show = shouldShowDivider,
                    strokeColor = LocalCuiPalette.current.OutlineSecondary,
                )
            )

            EmojiPicker(lazyGridState, state, onEmojiSelect)
        }

        Box(
            modifier = Modifier
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter)
        ) {
            CuiPrimaryButton(
                text = stringResource(CommonString.common_done),
                onClick = onDoneClick,
            )
        }
    }
}

@Composable
private fun EmojiPicker(
    lazyGridState: LazyGridState,
    state: TagCreationUiState,
    onEmojiSelect: (emoji: String) -> Unit
) {
    LazyVerticalGrid(
        state = lazyGridState,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        columns = GridCells.FixedSize(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        state.emojis.forEachIndexed { index, (category, emojis) ->
            item(key = category.name, contentType = "category", span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    if (index != 0) {
                        Spacer(Modifier.height(20.dp))
                    }

                    Text(
                        text = category.name,
                        fontSize = 14.sp,
                        color = LocalCuiPalette.current.TextSecondary,
                    )
                }
            }

            items(items = emojis, contentType = { "emoji" }, key = { it.emoji }) { item ->
                EmojiItem(onEmojiSelect, item)
            }
        }
    }
}

@Composable
private fun EmojiItem(onEmojiSelect: (emoji: String) -> Unit, item: CheckieEmoji) {
    IconButton(
        onClick = { onEmojiSelect(item.emoji) },
        modifier = Modifier.size(48.dp),
    ) {
        Text(
            text = item.emoji,
            fontSize = 24.sp,
        )
    }
}


@Composable
private fun Header(
    title: String,
    tagValue: String,
    tagValueError: String?,
    selectedEmoji: String?,
    isDeleteButtonAvailable: Boolean,
    focusRequester: FocusRequester,
    onDeleteTagClick: () -> Unit,
    onSelectedEmojiClick: () -> Unit,
    onTagValueInput: (value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = stringResource(R.string.tagcreation_description),
                    fontSize = 14.sp,
                    color = LocalCuiPalette.current.TextSecondary,
                )
            }

            if (isDeleteButtonAvailable) {
                CuiOutlineButton(
                    text = stringResource(R.string.tagcreation_delete),
                    onClick = onDeleteTagClick,
                    textColor = LocalCuiPalette.current.TextNegative,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            tint = LocalCuiPalette.current.IconNegative,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            CuiOutlinedField(
                text = tagValue,
                title = stringResource(R.string.tagcreation_tag_name),
                errorText = tagValueError,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                onValueChange = onTagValueInput,
                modifier = Modifier
                    .weight(1F)
                    .focusRequester(focusRequester)
            )

            IconButton(
                onClick = onSelectedEmojiClick,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(1.dp, LocalCuiPalette.current.OutlinePrimary, CircleShape)
            ) {
                if (selectedEmoji != null) {
                    Text(
                        text = selectedEmoji,
                        fontSize = 32.sp,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
    }
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    TagCreationScreen(state = mockUiState, focusRequester = FocusRequester())
}

internal val mockUiState = TagCreationUiState(
    title = "New tag",
    isInteractive = true,
    tagValue = "Tobacco",
    tagValueError = null,
    isDeleteAvailable = true,
    selectedEmoji = "\uD83D\uDC80", // 💀
    emojis = emptyPersistentList(),
)