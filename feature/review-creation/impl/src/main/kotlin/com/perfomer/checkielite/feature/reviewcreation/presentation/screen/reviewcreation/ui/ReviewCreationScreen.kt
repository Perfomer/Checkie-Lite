@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ProgressAppBar

@Composable
internal fun ReviewCreationScreen(
    state: ReviewCreationUiState,
    pagerState: PagerState,

    showExitDialog: Boolean = false,
    onExitDialogDismiss: () -> Unit = {},
    onExitDialogConfirm: () -> Unit = {},

    showErrorDialog: Boolean = false,
    onErrorDialogConfirm: () -> Unit = {},

    onPrimaryButtonClick: () -> Unit = {},
    onBackPress: () -> Unit = {},
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    Box(modifier = Modifier.background(LocalCuiPalette.current.BackgroundPrimary)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProgressAppBar(
                pagerState = pagerState,
                navigationIconPainter = painterResource(CommonDrawable.ic_arrow_back),
                firstStepNavigationIconPainter = painterResource(CommonDrawable.ic_cross),
                onBackPress = onBackPress,
                modifier = Modifier.statusBarsPadding()
            )

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                pageContent = content,
                modifier = Modifier.weight(1F),
            )
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(bottom = 20.dp)
                .padding(horizontal = 20.dp)
        ) {
            val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
            val targetButtonColor = if (isLastPage) LocalCuiPalette.current.BackgroundPositive else LocalCuiPalette.current.BackgroundAccentPrimary
            val animatedColor by animateColorAsState(
                targetValue = targetButtonColor,
                animationSpec = tween(durationMillis = 250),
                label = "AnimatedButtonColor",
            )

            val buttonText = if (isLastPage) stringResource(R.string.reviewcreation_save) else stringResource(R.string.reviewcreation_next)

            CuiPrimaryButton(
                text = buttonText,
                onClick = onPrimaryButtonClick,
                activeButtonColor = animatedColor,
                loading = state.isPrimaryButtonLoading,
            )
        }
    }

    if (showExitDialog) {
        ConfirmExitDialog(
            onDismiss = onExitDialogDismiss,
            onConfirm = onExitDialogConfirm,
        )
    }

    if (showErrorDialog) {
        ErrorDialog(
            onDismiss = { /* Do nothing */ },
            onConfirm = onErrorDialogConfirm,
        )
    }
}

@ScreenPreview
@Composable
private fun ReviewCreationScreenPreview() {
    ReviewCreationScreen(
        state = ReviewCreationUiState(
            step = 1,
            stepsCount = 2,
            currentPage = ReviewCreationPage.PRODUCT_INFO,
            productInfoState = ProductInfoPageUiState(
                productName = "",
                productNameErrorText = null,
                brand = "",
                picturesUri = emptyPersistentList(),
            ),
            reviewInfoState = ReviewInfoPageUiState(
                rating = 5,
                reviewText = "",
            ),
            isPrimaryButtonLoading = false,
        ),
        pagerState = rememberPagerState { 1 },
        content = {},
    )
}