package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.feature.reviewdetails.R

@Composable
internal fun EmptyText(
    onEmptyReviewTextClick: () -> Unit = {},
) {
    Text(
        text = stringResource(R.string.reviewdetails_empty_review_text),
        fontSize = 16.sp,
        color = CuiPalette.Light.TextSecondary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    TextButton(onClick = onEmptyReviewTextClick) {
        Text(
            text = stringResource(R.string.reviewdetails_add_review_text),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = CuiPalette.Light.TextAccent,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}