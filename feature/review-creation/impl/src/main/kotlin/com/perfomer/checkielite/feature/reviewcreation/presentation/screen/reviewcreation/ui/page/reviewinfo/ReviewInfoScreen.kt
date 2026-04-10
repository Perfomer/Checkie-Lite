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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.cui.widget.rating.RatingSlider
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.focusedFieldScrollContainer
import com.perfomer.checkielite.common.ui.util.focusedFieldScrollTarget
import com.perfomer.checkielite.common.ui.util.rememberFocusedFieldScroller
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.LocalObstruction
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.mockUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ReviewCreationPageHeader

@Composable
internal fun ReviewInfoScreen(
    state: ReviewInfoPageUiState,
    scrollState: ScrollState = rememberScrollState(),
    commentFocusRequester: FocusRequester = remember { FocusRequester() },
    onRatingSelect: (Int) -> Unit = {},
    onCommentInput: (String) -> Unit = {},
    onAdvantagesInput: (String) -> Unit = {},
    onDisadvantagesInput: (String) -> Unit = {},
) {
    val obstruction = LocalObstruction.current
    val fieldScroller = rememberFocusedFieldScroller<String>(
        scrollState = scrollState,
        bottomObstruction = obstruction.calculateBottomPadding(),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusedFieldScrollContainer(fieldScroller)
            .verticalScroll(scrollState)
            .navigationBarsPadding()
            .imePadding()
            .padding(24.dp)
    ) {
        ReviewCreationPageHeader(
            title = stringResource(R.string.reviewcreation_reviewinfo_title),
            productPictureUri = state.mainPictureUri,
            productName = state.productName,
        )

        CuiSpacer(12.dp)

        RatingSlider(
            rating = state.rating,
            onRatingChange = onRatingSelect,
            isEnabled = !state.isSaving,
        )

        CuiSpacer(24.dp)

        CuiOutlinedField(
            text = state.comment,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_comment),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onCommentInput,
            modifier = Modifier
                .focusRequester(commentFocusRequester)
                .focusedFieldScrollTarget("comment", fieldScroller)
        )

        CuiSpacer(4.dp)

        CuiOutlinedField(
            text = state.advantages,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_advantages),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onAdvantagesInput,
            modifier = Modifier.focusedFieldScrollTarget("advantages", fieldScroller)
        )

        Spacer(Modifier.height(4.dp))

        CuiOutlinedField(
            text = state.disadvantages,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_disadvantages),
            singleLine = false,
            isEnabled = !state.isSaving,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onDisadvantagesInput,
            modifier = Modifier.focusedFieldScrollTarget("disadvantages", fieldScroller)
        )

        CuiSpacer(obstruction.calculateBottomPadding() + 24.dp)
    }
}

@Composable
@ScreenPreview
private fun ReviewInfoScreenPreview() {
    ReviewInfoScreen(
        state = mockUiState.reviewInfoState,
    )
}
