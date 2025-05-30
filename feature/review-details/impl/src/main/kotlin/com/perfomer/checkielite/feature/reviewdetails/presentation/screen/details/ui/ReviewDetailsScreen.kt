package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.copy
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.RecommendedReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsAppBar
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsHeader
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsImage
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsInfo
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsRecommendations
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget.ReviewDetailsTags
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
    onEmptyPriceClick: () -> Unit = {},
    onEmptyReviewTextClick: () -> Unit = {},
    onPageChange: (pageIndex: Int) -> Unit = {},
    onAddTagsClick: () -> Unit = {},
    onTagClick: (tagId: String) -> Unit = {},
    onRecommendationClick: (recommendedReviewId: String) -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            ReviewDetailsAppBar(
                scrollState = scrollState,
                title = (state as? ReviewDetailsUiState.Content)?.productName,
                isMenuAvailable = state.isMenuAvailable,
                onNavigationIconClick = onNavigationIconClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        },
    ) { contentPadding ->
        when (state) {
            is ReviewDetailsUiState.Loading -> Loading()
            is ReviewDetailsUiState.Content -> Content(
                state = state,
                contentPadding = contentPadding,
                scrollableState = scrollState,
                onPictureClick = onPictureClick,
                onEmptyImageClick = onEmptyImageClick,
                onEmptyPriceClick = onEmptyPriceClick,
                onEmptyReviewTextClick = onEmptyReviewTextClick,
                onPageChange = onPageChange,
                onAddTagsClick = onAddTagsClick,
                onTagClick = onTagClick,
                onRecommendationClick = onRecommendationClick,
            )

            is ReviewDetailsUiState.Error -> Error()

        }

        if (showDeleteDialog) {
            ConfirmDeleteDialog(
                onDismiss = onDeleteDialogDismiss,
                onConfirm = onDeleteDialogConfirm,
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Content(
    state: ReviewDetailsUiState.Content,
    contentPadding: PaddingValues,
    scrollableState: LazyListState,
    onPictureClick: () -> Unit,
    onEmptyImageClick: () -> Unit,
    onEmptyPriceClick: () -> Unit,
    onEmptyReviewTextClick: () -> Unit,
    onPageChange: (pageIndex: Int) -> Unit,
    onAddTagsClick: () -> Unit,
    onTagClick: (tagId: String) -> Unit,
    onRecommendationClick: (recommendedReviewId: String) -> Unit,
) {
    LazyColumn(
        state = scrollableState,
        contentPadding = contentPadding.copy(bottom = contentPadding.calculateBottomPadding() + 24.dp),
    ) {
        item {
            ReviewDetailsHeader(
                productName = state.productName,
                brandName = state.brandName,
            )
        }

        item {
            ReviewDetailsImage(
                picturesUri = state.picturesUri,
                currentPicturePosition = state.currentPicturePosition,
                onEmptyImageClick = onEmptyImageClick,
                onPictureClick = onPictureClick,
                onPageChange = onPageChange
            )
        }

        item {
            ReviewDetailsInfo(
                date = state.date,
                rating = state.rating,
                price = state.price,
                onEmptyPriceClick = onEmptyPriceClick,
            )
        }

        item {
            ReviewDetailsText(
                comment = state.comment,
                advantages = state.advantages,
                disadvantages = state.disadvantages,
                onEmptyCommentClick = onEmptyReviewTextClick,
            )
        }

        item {
            ReviewDetailsTags(
                tags = state.tags,
                onAddTagsClick = onAddTagsClick,
                onTagClick = onTagClick,
            )
        }

        item {
            ReviewDetailsRecommendations(
                recommendations = state.recommendations,
                onRecommendationClick = onRecommendationClick,
            )
        }
    }
}

@Composable
private fun Error() {
    CuiBlock(
        title = stringResource(CommonString.common_error_title),
        message = stringResource(CommonString.common_error_message),
        illustrationPainter = painterResource(CommonDrawable.ill_error),
    )
}

@ScreenPreview
@Composable
private fun ReviewDetailsScreenPreview() = CheckieLiteTheme {
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
    date = "31 May 2023",
    price = null,
    currentPicturePosition = 0,
    comment = "Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented.",
    advantages = "Great, but not great.",
    disadvantages = "It's okay.",
    isMenuAvailable = true,
    tags = persistentListOf(),
    recommendations = persistentListOf(
        RecommendedReview("", "DARKSIDE", "Lemonblast", null, 10, false),
    )
)