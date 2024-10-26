package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChip
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiChipStyle
import com.perfomer.checkielite.common.ui.cui.widget.chip.CuiTagChip
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedFieldDefaults
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.mockUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagsScreen(
    state: TagsPageUiState,
    scrollState: ScrollState = rememberScrollState(),
    onTagSortClick: () -> Unit = {},
    onCreateTagClick: () -> Unit = {},
    onTagClick: (id: String) -> Unit = {},
    onTagLongClick: (id: String) -> Unit = {},
    onSearchQueryInput: (text: String) -> Unit = {},
    onSearchQueryClearClick: () -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 104.dp, top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.reviewcreation_tags_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1F)
            )

            CuiIconButton(
                painter = painterResource(CommonDrawable.ic_sort),
                onClick = onTagSortClick,
            )
        }

        CuiSpacer(16.dp)

        SearchField(
            searchQuery = state.searchQuery,
            onSearchQueryInput = onSearchQueryInput,
            onSearchQueryClearClick = {
                onSearchQueryClearClick()
                focusManager.clearFocus()
            },
        )

        CuiSpacer(16.dp)

        @Composable
        fun TagChip(tag: TagsPageUiState.Tag) {
            CuiTagChip(
                text = tag.value,
                emoji = tag.emoji,
                isSelected = tag.isSelected,
                onClick = { onTagClick(tag.id) },
                onLongClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onTagLongClick(tag.id)
                },
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.shouldShowAddTag) {
                AddTagChip(
                    searchQuery = state.searchQuery.takeIf { it.isNotBlank() },
                    onCreateTagClick = {
                        focusManager.clearFocus()
                        onCreateTagClick()
                    },
                )
            }

            for (tag in state.tags) {
                key(tag.id) { TagChip(tag) }
            }
        }
    }
}

@Composable
private fun AddTagChip(
    searchQuery: String?,
    onCreateTagClick: () -> Unit
) {
    val palette = LocalCuiPalette.current

    val addTagStyle = remember {
        CuiChipStyle(
            iconBackgroundColor = palette.BackgroundAccentSecondary,
            textBackgroundColor = palette.BackgroundPrimary,
            borderColor = palette.OutlineAccentSecondary,
            borderWidth = 1.dp,
            fontWeight = FontWeight.Medium,
        )
    }

    val addTagText = if (searchQuery == null) {
        stringResource(R.string.reviewcreation_tags_create_tag_generic)
    } else {
        stringResource(R.string.reviewcreation_tags_create_tag_specific, searchQuery)
    }

    CuiChip(
        leadingIcon = {
            Icon(
                painter = painterResource(CommonDrawable.ic_plus),
                contentDescription = null,
                tint = palette.TextAccent,
                modifier = Modifier.size(16.dp),
            )
        },
        style = addTagStyle,
        onClick = onCreateTagClick,
    ) {
        Text(addTagText)
    }
}

@Composable
private fun SearchField(
    searchQuery: String,
    onSearchQueryInput: (text: String) -> Unit,
    onSearchQueryClearClick: () -> Unit,
) {
    CuiOutlinedField(
        text = searchQuery,
        placeholder = stringResource(R.string.reviewcreation_tags_field_search),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        singleLine = true,
        onValueChange = onSearchQueryInput,
        colors = CuiOutlinedFieldDefaults.colors(unfocusedBorderColor = LocalCuiPalette.current.OutlineSecondary),
        trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(
                    painter = painterResource(id = CommonDrawable.ic_search),
                    tint = LocalCuiPalette.current.IconSecondary,
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            } else {
                CuiIconButton(
                    painter = painterResource(id = CommonDrawable.ic_cross),
                    contentDescription = stringResource(CommonString.common_clear),
                    tint = LocalCuiPalette.current.IconPrimary,
                    onClick = onSearchQueryClearClick,
                    modifier = Modifier.offset(x = (-4).dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@ScreenPreview
private fun TagsScreenPreview() {
    TagsScreen(
        state = mockUiState.tagsState,
    )
}