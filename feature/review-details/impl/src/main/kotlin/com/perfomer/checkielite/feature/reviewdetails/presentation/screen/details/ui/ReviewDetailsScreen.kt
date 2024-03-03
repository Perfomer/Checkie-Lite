package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsAppBar
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsHeader
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsImage
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsText
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ReviewDetailsScreen(
    state: ReviewDetailsUiState,
    showDeleteDialog: Boolean = false,
    onDeleteDialogDismiss: () -> Unit = {},
    onDeleteDialogConfirm: () -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onPictureClick: () -> Unit = {},
    onEmptyImageClick: () -> Unit = {},
    onEmptyReviewTextClick: () -> Unit = {},
    onPageChange: (pageIndex: Int) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ReviewDetailsAppBar(
                scrollState = scrollState,
                onNavigationIconClick = onNavigationIconClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        },
    ) { contentPadding ->
        if (state !is ReviewDetailsUiState.Content) return@Scaffold

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(contentPadding)
                .padding(bottom = 16.dp)
        ) {
            ReviewDetailsHeader(
                productName = state.productName,
                brandName = state.brandName,
                date = state.date,
                emoji = state.emoji,
                rating = state.rating
            )

            ReviewDetailsImage(
                picturesUri = state.picturesUri,
                currentPicturePosition = state.currentPicturePosition,
                onEmptyImageClick = onEmptyImageClick,
                onPictureClick = onPictureClick,
                onPageChange = onPageChange
            )

            Spacer(Modifier.height(24.dp))

            ReviewDetailsText(
                reviewText = state.reviewText,
                onEmptyReviewTextClick = onEmptyReviewTextClick,
            )
        }

        if (showDeleteDialog) {
            ConfirmDeleteDialog(
                onDismiss = onDeleteDialogDismiss,
                onConfirm = onDeleteDialogConfirm,
            )
        }
    }
}


@ScreenPreview
@Composable
private fun ReviewDetailsScreenPreview() {
    ReviewDetailsScreen(state = mockUiState)
}

internal val mockUiState = ReviewDetailsUiState.Content(
    productName = "Chicken toasts with poached eggs",
    brandName = "LUI BIDON",
    picturesUri = persistentListOf(
        "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
        "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
        "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
    ),
    rating = 8,
    emoji = "\uD83D\uDE0D", // 😍
    date = "31 May 2023",
    currentPicturePosition = 0,
    reviewText = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
)