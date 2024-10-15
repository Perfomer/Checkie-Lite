package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.modifier.shake
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiSecondaryButton
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.widget.ConfirmDeleteDialog

@Composable
internal fun TagCreationScreen(
    state: TagCreationUiState,

    focusRequester: FocusRequester = FocusRequester(),
    tagValueShakeController: ShakeController = rememberShakeController(),

    isConfirmDeleteDialogShown: Boolean = false,
    onDeleteDialogDismiss: () -> Unit = {},
    onDeleteDialogConfirm: () -> Unit = {},

    onTagValueInput: (value: String) -> Unit = {},
    onDoneClick: () -> Unit = {},
    onDeleteTagClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsPadding()
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp)
    ) {
        Header(title = state.title)

        CuiOutlinedField(
            text = state.tagValue,
            title = stringResource(R.string.tagcreation_tag_name),
            errorText = state.tagValueError,
            reservePlaceForError = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            onValueChange = onTagValueInput,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .shake(tagValueShakeController)
        )

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
        }

        CuiPrimaryButton(
            text = stringResource(CommonString.common_done),
            onClick = onDoneClick,
        )
    }

    if (isConfirmDeleteDialogShown) {
        ConfirmDeleteDialog(
            onDismiss = onDeleteDialogDismiss,
            onConfirm = onDeleteDialogConfirm
        )
    }
}

@Composable
private fun Header(title: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
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