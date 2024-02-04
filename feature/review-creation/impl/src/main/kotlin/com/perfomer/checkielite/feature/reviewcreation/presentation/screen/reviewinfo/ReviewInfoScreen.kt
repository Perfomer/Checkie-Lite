package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.cui.field.CuiOutlinedField
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.impl.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo.widget.RatingSlider

@Composable
internal fun ReviewInfoScreen(
    state: ReviewInfoPageUiState,
    onRatingSelect: (Int) -> Unit = {},
    onReviewTextInput: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.reviewcreation_reviewinfo_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        RatingSlider(
            rating = state.rating,
            onRatingChange = onRatingSelect,
        )

        Spacer(Modifier.height(24.dp))

        CuiOutlinedField(
            text = state.reviewText,
            title = stringResource(R.string.reviewcreation_reviewinfo_field_review),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = false,
            onValueChange = onReviewTextInput,
        )
    }
}

@Composable
@ScreenPreview
private fun ReviewInfoScreenPreview() {
    ReviewInfoScreen(
        state = ReviewInfoPageUiState(
            rating = 5,
            reviewText = "",
        )
    )
}