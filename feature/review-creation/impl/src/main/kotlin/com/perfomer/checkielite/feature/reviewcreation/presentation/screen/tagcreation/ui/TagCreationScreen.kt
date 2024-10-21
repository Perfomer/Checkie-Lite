package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiSecondaryButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.scrim.verticalScrimBrush
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.keyboardOpenedAsState
import com.perfomer.checkielite.common.ui.util.plus
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.widget.ConfirmDeleteDialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

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
    onEmojiSelect: (item: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDeleteTagClick: () -> Unit = {},
) {
    val scrollState = rememberLazyGridState()

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
            lazyGridState = scrollState,
            emojis = state.emojis,
        )

        Box(modifier = Modifier.weight(1F)) {
            EmojiPicker(
                lazyGridState = scrollState,
                emojis = state.emojis,
                contentPadding = actualNavigationBarsPadding + PaddingValues(bottom = 96.dp),
                onEmojiSelect = onEmojiSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            FloatingButtonsContainer(
                isDeleteAvailable = state.isDeleteAvailable,
                onDoneClick = onDoneClick,
                onDeleteTagClick = onDeleteTagClick,
            )
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

    lazyGridState: LazyGridState,
    emojis: ImmutableList<TagCreationEmojiCategory>,

    modifier: Modifier = Modifier,
) {
    val shouldShowDivider by remember { derivedStateOf { lazyGridState.canScrollBackward } }

    Column(
        modifier = modifier
            .bottomStrokeOnScroll(
                show = shouldShowDivider,
                strokeColor = LocalCuiPalette.current.OutlineSecondary,
            )
    ) {
        SheetTitle(title = title)

        Spacer(Modifier.height(16.dp))

        TagContent(
            tagValue = tagValue,
            tagValueError = tagValueError,
            tagValueFocusRequester = tagValueFocusRequester,
            tagValueShakeController = tagValueShakeController,
            onTagValueInput = onTagValueInput,
            selectedEmoji = selectedEmoji,
            onSelectedEmojiClick = onSelectedEmojiClick,
        )

        EmojiCategoriesCarousel(
            lazyGridState = lazyGridState,
            emojis = emojis,
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SheetTitle(title: String) {
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
}

@Composable
private fun TagContent(
    tagValue: String,
    tagValueError: String?,
    tagValueFocusRequester: FocusRequester,
    tagValueShakeController: ShakeController,
    onTagValueInput: (value: String) -> Unit,

    selectedEmoji: String?,
    onSelectedEmojiClick: () -> Unit,
) {
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
}

@Composable
private fun EmojiCategoriesCarousel(
    lazyGridState: LazyGridState,
    emojis: ImmutableList<TagCreationEmojiCategory>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val categoriesScrollState = rememberLazyListState()

    val categoriesIndexes = remember(emojis) {
        val result = mutableListOf(0)
        var counter = 0

        for (category in emojis) {
            counter += category.emojis.size + 1
            result += counter
        }

        result.toPersistentList()
    }

    val currentIndex by remember {
        derivedStateOf {
            categoriesIndexes.indexOfLast {
                lazyGridState.firstVisibleItemIndex >= it
            }
        }
    }

    UpdateEffect(currentIndex) {
        categoriesScrollState.animateScrollToItem(currentIndex)
    }

    LazyRow(
        state = categoriesScrollState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(items = emojis) { i, category ->
            CategoryIcon(
                icon = painterResource(
                    when (i) {
                        0 -> R.drawable.ic_emoji_category_smiles
                        1 -> R.drawable.ic_emoji_category_body
                        2 -> R.drawable.ic_emoji_category_animals
                        3 -> R.drawable.ic_emoji_category_food
                        4 -> R.drawable.ic_emoji_category_travel
                        5 -> R.drawable.ic_emoji_category_activities
                        6 -> R.drawable.ic_emoji_category_objects
                        7 -> R.drawable.ic_emoji_category_flags
                        else -> R.drawable.ic_emoji_category_flags
                    }
                ),
                isSelected = currentIndex == i,
                onClick = {
                    coroutineScope.launch {
                        lazyGridState.animateScrollToItem(categoriesIndexes[i])
                    }
                }
            )
        }
    }
}

@Composable
private fun CategoryIcon(
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    CuiIconButton(
        painter = icon,
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .alpha(if (isSelected) 1F else 0.3F)
    )
}

@Composable
private fun EmojiPicker(
    lazyGridState: LazyGridState,
    emojis: ImmutableList<TagCreationEmojiCategory>,
    onEmojiSelect: (item: String) -> Unit,
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
        emojis.forEachIndexed { index, category ->
            item(key = category.name, contentType = "category", span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(isFirst = index == 0, name = category.name)
            }

            items(items = category.emojis, contentType = { "emoji" }) { item ->
                EmojiItem(item, onEmojiSelect)
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    isFirst: Boolean,
    name: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .padding(horizontal = 8.dp)
    ) {
        if (!isFirst) {
            Spacer(Modifier.height(20.dp))
        }

        Text(
            text = name,
            fontSize = 14.sp,
            color = LocalCuiPalette.current.TextSecondary,
        )
    }
}

@Composable
private fun EmojiItem(
    item: String,
    onEmojiSelect: (item: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = { onEmojiSelect(item) },
        modifier = modifier.size(56.dp),
    ) {
        Text(
            text = item,
            fontSize = 24.sp,
        )
    }
}

@Composable
private fun BoxScope.FloatingButtonsContainer(
    isDeleteAvailable: Boolean,
    onDoneClick: () -> Unit,
    onDeleteTagClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .imePadding()
            .background(verticalScrimBrush())
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .align(Alignment.BottomCenter)
    ) {
        if (isDeleteAvailable) {
            CuiSecondaryButton(
                text = stringResource(R.string.tagcreation_delete),
                onClick = onDeleteTagClick,
                textColor = LocalCuiPalette.current.TextNegative,
                backgroundColor = LocalCuiPalette.current.BackgroundElevationBase,
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