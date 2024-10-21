package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiSecondaryButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.keyboardOpenedAsState
import com.perfomer.checkielite.common.ui.util.plus
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmoji
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.widget.ConfirmDeleteDialog
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TagCreationScreen(
    state: TagCreationUiState,

    tagValueFocusRequester: FocusRequester = FocusRequester(),
    tagValueShakeController: ShakeController = rememberShakeController(),

    isConfirmDeleteDialogShown: Boolean = false,
    onDeleteDialogDismiss: () -> Unit = {},
    onDeleteDialogConfirm: () -> Unit = {},

    onTagValueInput: (value: String) -> Unit = {},
    onSelectedEmojiClick: () -> Unit = {},
    onEmojiSelect: (item: CheckieEmoji) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDeleteTagClick: () -> Unit = {},
) {
    val scrollState = rememberLazyGridState()
    val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

    val isKeyboardOpened by keyboardOpenedAsState()
    val emptyInsets = remember { PaddingValues() }
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
    val actualNavigationBarsPadding = if (isKeyboardOpened) emptyInsets else navigationBarsPadding


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9F)
    ) {
        Header(
            title = state.title,
            tagValue = state.tagValue,
            tagValueError = state.tagValueError,
            tagValueFocusRequester = tagValueFocusRequester,
            tagValueShakeController = tagValueShakeController,
            selectedEmoji = state.selectedEmoji,
            onSelectedEmojiClick = onSelectedEmojiClick,
            onTagValueInput = onTagValueInput,
            modifier = Modifier
                .bottomStrokeOnScroll(
                    show = shouldShowDivider,
                    strokeColor = LocalCuiPalette.current.OutlineSecondary,
                )
        )

        Box(modifier = Modifier.weight(1F)) {
            EmojiPicker(
                lazyGridState = scrollState,
                emojis = state.emojis,
                contentPadding = actualNavigationBarsPadding + PaddingValues(bottom = 96.dp),
                onEmojiSelect = onEmojiSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter)
            ) {
                if (state.isDeleteAvailable) {
                    CuiSecondaryButton(
                        text = stringResource(R.string.tagcreation_delete),
                        onClick = onDeleteTagClick,
                        textColor = LocalCuiPalette.current.TextNegative,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_trash),
                                tint = LocalCuiPalette.current.IconNegative,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                    )

                    Spacer(Modifier.height(12.dp))
                }

                CuiPrimaryButton(
                    text = stringResource(CommonString.common_done),
                    onClick = onDoneClick,
                )
            }
        }
    }

    if (isConfirmDeleteDialogShown) {
        ConfirmDeleteDialog(
            onDismiss = onDeleteDialogDismiss,
            onConfirm = onDeleteDialogConfirm
        )
    }
}

@Composable
private fun Header(
    title: String,
    tagValue: String,
    tagValueError: String?,
    tagValueFocusRequester: FocusRequester,
    tagValueShakeController: ShakeController,
    selectedEmoji: String?,
    onSelectedEmojiClick: () -> Unit,
    onTagValueInput: (value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
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

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            CuiOutlinedField(
                text = tagValue,
                title = stringResource(R.string.tagcreation_tag_name),
                errorText = tagValueError,
                reservePlaceForError = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                onValueChange = onTagValueInput,
                modifier = Modifier
                    .weight(1F)
                    .focusRequester(tagValueFocusRequester)
                    .shake(tagValueShakeController)
            )

            IconButton(
                onClick = onSelectedEmojiClick,
                modifier = Modifier
                    .padding(top = 10.dp)
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

@Composable
private fun EmojiPicker(
    lazyGridState: LazyGridState,
    emojis: ImmutableList<CheckieEmojiCategory>,
    onEmojiSelect: (item: CheckieEmoji) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.FixedSize(56.dp),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        emojis.forEachIndexed { index, emojis ->
            item(key = emojis.name, contentType = "category", span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    if (index != 0) {
                        Spacer(Modifier.height(20.dp))
                    }

                    Text(
                        text = emojis.name,
                        fontSize = 14.sp,
                        color = LocalCuiPalette.current.TextSecondary,
                    )
                }
            }

            emojis.groups.forEach {
                items(items = it.emojis) { item ->
                    EmojiItem(onEmojiSelect, item)
                }
            }

        }
    }
}

@Composable
private fun EmojiItem(onEmojiSelect: (item: CheckieEmoji) -> Unit, item: CheckieEmoji) {
    IconButton(
        onClick = { onEmojiSelect(item) },
        modifier = Modifier.size(56.dp),
    ) {
        Text(
            text = item.char,
            fontSize = 24.sp,
        )
    }
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() = CheckieLiteTheme {
    TagCreationScreen(state = mockUiState)
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