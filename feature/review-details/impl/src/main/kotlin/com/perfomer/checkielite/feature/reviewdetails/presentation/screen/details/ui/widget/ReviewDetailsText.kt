package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ReviewDetailsText(
    comment: String?,
    advantages: String?,
    disadvantages: String?,
    onEmptyCommentClick: () -> Unit,
) {
    Spacer(Modifier.height(8.dp))

    if (comment != null) {
        Spacer(Modifier.height(16.dp))

        ReviewDetailsTextCard(
            text = comment,
            header = { ReviewDetailsCommentHeader() },
        )
    } else if (advantages == null && disadvantages == null) {
        Spacer(Modifier.height(16.dp))

        ReviewDetailsTextEmptyCard(
            onClick = onEmptyCommentClick,
        )
    }

    if (advantages != null) {
        Spacer(Modifier.height(16.dp))

        ReviewDetailsTextCard(
            text = advantages,
            header = { ReviewDetailsAdvantagesHeader() },
        )
    }

    if (disadvantages != null) {
        Spacer(Modifier.height(16.dp))

        ReviewDetailsTextCard(
            text = disadvantages,
            header = { ReviewDetailsDisadvantagesHeader() },
        )
    }
}