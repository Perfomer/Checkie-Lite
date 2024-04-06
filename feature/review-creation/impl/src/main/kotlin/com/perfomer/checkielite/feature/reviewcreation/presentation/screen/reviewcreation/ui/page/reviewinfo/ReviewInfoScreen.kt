package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.reviewinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.rating.RatingSlider
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState

@Composable
internal fun ReviewInfoScreen(
    state: ReviewInfoPageUiState,
    scrollState: ScrollState = rememberScrollState(),
    onRatingSelect: (Int) -> Unit = {},
    onCommentInput: (String) -> Unit = {},
    onAdvantagesInput: (String) -> Unit = {},
    onDisadvantagesInput: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .imePadding()
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_reviewinfo_title),
            fontSize = 24.sp,
            color = LocalCuiPalette.current.TextPrimary,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        RatingSlider(
            rating = state.rating,
            onRatingChange = onRatingSelect,
            isEnabled = !state.isSaving,
        )

        Spacer(Modifier.height(24.dp))

        CuiOutlinedField(
            text = state.comment,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_comment),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onCommentInput,
            modifier = Modifier.focusRequester(focusRequester)
        )

        Spacer(Modifier.height(4.dp))

        CuiOutlinedField(
            text = state.advantages,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_advantages),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onAdvantagesInput,
        )

        Spacer(Modifier.height(4.dp))

        CuiOutlinedField(
            text = state.disadvantages,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_disadvantages),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onDisadvantagesInput,
        )
    }
}

@Composable
@ScreenPreview
private fun ReviewInfoScreenPreview() {
    ReviewInfoScreen(
        state = ReviewInfoPageUiState(
            rating = 5,
            comment = "",
            advantages = "",
            disadvantages = "",
            isSaving = false,
        )
    )
}