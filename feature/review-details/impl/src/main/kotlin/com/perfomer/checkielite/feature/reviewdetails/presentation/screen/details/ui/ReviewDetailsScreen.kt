package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.block.CuiBlock
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiGlassScaffold
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationLazyListItem
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationContainer
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.add
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationContent
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
    onRatingClick: () -> Unit = {},
    onEmptyPriceClick: () -> Unit = {},
    onEmptyReviewTextClick: () -> Unit = {},
    onPageChange: (pageIndex: Int) -> Unit = {},
    onAddTagsClick: () -> Unit = {},
    onTagClick: (tagId: String) -> Unit = {},
    onRecommendationClick: (recommendedReviewId: String) -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val palette = LocalCuiPalette.current
    val backgroundColor = lerp(palette.BackgroundPrimary, palette.BackgroundAccentTertiary, 0.4F)
    val toolbarFadeDistance = with(LocalDensity.current) { 24.dp.toPx() }

    SharedNavigationContent(
        id = (state as? ReviewDetailsUiState.Content)?.reviewId,
    ) {
        SharedNavigationContainer(
            cornerRadius = 0.dp,
            overlayCornerRadius = 0.dp,
            color = backgroundColor,
            overlayColor = backgroundColor,
            modifier = Modifier.fillMaxSize()
        ) {
            CuiGlassScaffold(
                containerColor = Color.Transparent,
                toolbarColor = backgroundColor,
                toolbarBackgroundProgress = {
                    when {
                        !scrollState.canScrollBackward -> 0F
                        scrollState.firstVisibleItemIndex > 0 -> 1F
                        else -> (scrollState.firstVisibleItemScrollOffset / toolbarFadeDistance).coerceIn(0F, 1F)
                    }
                },
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
                        onRatingClick = onRatingClick,
                        onEmptyPriceClick = onEmptyPriceClick,
                        onEmptyReviewTextClick = onEmptyReviewTextClick,
                        onPageChange = onPageChange,
                        onAddTagsClick = onAddTagsClick,
                        onTagClick = onTagClick,
                        onRecommendationClick = onRecommendationClick,
                    )

                    is ReviewDetailsUiState.Error -> Error()
                }

                ConfirmDeleteDialog(
                    isVisible = showDeleteDialog,
                    onDismiss = onDeleteDialogDismiss,
                    onConfirm = onDeleteDialogConfirm,
                )
            }
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
@OptIn(ExperimentalMaterial3Api::class)
private fun Content(
    state: ReviewDetailsUiState.Content,
    contentPadding: PaddingValues,
    scrollableState: LazyListState,
    onPictureClick: () -> Unit,
    onEmptyImageClick: () -> Unit,
    onRatingClick: () -> Unit,
    onEmptyPriceClick: () -> Unit,
    onEmptyReviewTextClick: () -> Unit,
    onPageChange: (pageIndex: Int) -> Unit,
    onAddTagsClick: () -> Unit,
    onTagClick: (tagId: String) -> Unit,
    onRecommendationClick: (recommendedReviewId: String) -> Unit,
) = CompositionLocalProvider(
    LocalRippleConfiguration provides RippleConfiguration(
        color = lerp(
            LocalCuiPalette.current.BackgroundAccentPrimary,
            Color.White,
            0.5F,
        ),
    ),
) {
    LazyColumn(
        state = scrollableState,
        contentPadding = contentPadding.add(bottom = 24.dp),
    ) {
        item(key = "header") {
            SharedNavigationLazyListItem(
                id = state.reviewId,
                listState = scrollableState,
                itemKey = "header",
            ) {
                ReviewDetailsHeader(
                    productName = state.productName,
                    brandName = state.brandName,
                )
            }
        }

        item(key = "pictures") {
            SharedNavigationLazyListItem(
                id = state.reviewId,
                listState = scrollableState,
                itemKey = "pictures",
            ) {
                ReviewDetailsImage(
                    picturesUri = state.picturesUri,
                    currentPicturePosition = state.currentPicturePosition,
                    onEmptyImageClick = onEmptyImageClick,
                    onPictureClick = onPictureClick,
                    onPageChange = onPageChange,
                )
            }
        }

        item(key = "info") {
            ReviewDetailsInfo(
                date = state.date,
                rating = state.rating,
                price = state.price,
                onRatingClick = onRatingClick,
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

        item(key = "recommendations") {
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
    reviewId = "preview",
    productName = Text.raw("Chicken toasts with poached eggs"),
    brandName = Text.raw("LUI BIDON"),
    picturesUri = persistentListOf(
        "https://habrastorage.org/r/w780/getpro/habr/upload_files/746/2ab/27c/7462ab27cca552ce31ee9cba01387692.jpeg",
        "https://images.unsplash.com/photo-1483129804960-cb1964499894?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        "https://images.unsplash.com/photo-1620447875063-19be4e4604bc?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=796&q=80",
        "https://images.unsplash.com/photo-1548100535-fe8a16c187ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1151&q=80"
    ),
    rating = 8,
    date = Text.raw("31 May 2023"),
    price = null,
    currentPicturePosition = 0,
    comment = Text.raw("Extraordinary. Meets an elite standard by which you judge all other restaurants. The staff is always ready to help, the premises are extremely clean, the atmosphere is lovely, and the food is both delicious and beautifully presented."),
    advantages = Text.raw("Great, but not great."),
    disadvantages = Text.raw("It's okay."),
    isMenuAvailable = true,
    tags = persistentListOf(),
    recommendations = persistentListOf(
        RecommendedReview(
            reviewId = "",
            brandName = Text.raw("DARKSIDE"),
            productName = Text.raw("Lemonblast"),
            pictureUri = null,
            rating = 10,
            isSyncing = false,
        ),
    )
)
