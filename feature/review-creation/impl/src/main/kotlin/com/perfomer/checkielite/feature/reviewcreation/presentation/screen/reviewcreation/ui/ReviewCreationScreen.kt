@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.bottomStrokeOnScroll
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.cui.widget.scrim.verticalScrimBrush
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.TagsPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ProgressAppBar
import kotlinx.collections.immutable.persistentListOf

internal val LocalObstruction = staticCompositionLocalOf { PaddingValues() }

@Composable
internal fun ReviewCreationScreen(
    state: ReviewCreationUiState,
    pagerState: PagerState,
    shouldShowTopDivider: Boolean,

    showExitDialog: Boolean = false,
    onExitDialogDismiss: () -> Unit = {},
    onExitDialogConfirm: () -> Unit = {},

    showErrorDialog: Boolean = false,
    onErrorDialogConfirm: () -> Unit = {},

    onPrimaryButtonClick: () -> Unit = {},
    onBackPress: () -> Unit = {},
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    val density = LocalDensity.current
    var bottomButtonHeightPx by remember { mutableIntStateOf(0) }
    val obstruction = with(density) { PaddingValues(bottom = bottomButtonHeightPx.toDp()) }

    Box(modifier = Modifier.background(LocalCuiPalette.current.BackgroundPrimary)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProgressAppBar(
                pagerState = pagerState,
                navigationIconPainter = painterResource(CommonDrawable.ic_arrow_back),
                firstStepNavigationIconPainter = painterResource(CommonDrawable.ic_cross),
                onBackPress = onBackPress,
                modifier = Modifier.bottomStrokeOnScroll(
                    show = shouldShowTopDivider,
                    strokeColor = LocalCuiPalette.current.OutlineSecondary,
                )
            )

            CompositionLocalProvider(LocalObstruction provides obstruction) {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    pageContent = { page -> content(page) },
                    modifier = Modifier.weight(1F),
                )
            }
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding()
                .background(verticalScrimBrush())
                .padding(bottom = 20.dp)
                .navigationBarsPadding()
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .onSizeChanged { bottomButtonHeightPx = it.height }
            ) {
                val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
                val targetButtonColor = if (isLastPage) LocalCuiPalette.current.BackgroundPositivePrimary else LocalCuiPalette.current.BackgroundAccentPrimary
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    ConfirmExitDialog(
        isVisible = showExitDialog,
        onDismiss = onExitDialogDismiss,
        onConfirm = onExitDialogConfirm,
    )

    ErrorDialog(
        isVisible = showErrorDialog,
        onDismiss = { /* Do nothing */ },
        onConfirm = onErrorDialogConfirm,
    )
}

@ScreenPreview
@Composable
private fun ReviewCreationScreenPreview() {
    ReviewCreationScreen(
        state = mockUiState,
        pagerState = rememberPagerState { 1 },
        shouldShowTopDivider = false,
        content = { _ -> },
    )
}

internal val mockUiState = ReviewCreationUiState(
    step = 1,
    stepsCount = 3,
    currentPage = ReviewCreationPage.PRODUCT_INFO,
    productInfoState = ProductInfoPageUiState(
        productName = "Chips",
        productNameErrorText = null,
        brand = "",
        brandSuggestions = emptyPersistentList(),
        price = "0",
        priceCurrency = "RUB",
        picturesUri = emptyPersistentList(),
    ),
    tagsState = TagsPageUiState(
        mainPictureUri = null,
        productName = "Chips",
        searchQuery = "",
        shouldShowAddTag = true,
        tags = persistentListOf(
            TagsPageUiState.Tag("", "tag4", null, true, false),
            TagsPageUiState.Tag("", "tag5", "\uD83C\uDF47", true, false),
            TagsPageUiState.Tag("", "tag1", null, false, true),
            TagsPageUiState.Tag("", "tag2", "\uD83C\uDF47", false, true),
            TagsPageUiState.Tag("", "tag3", null, false, true),
            TagsPageUiState.Tag("", "tag6", null, false, false),
        ),
    ),
    reviewInfoState = ReviewInfoPageUiState(
        mainPictureUri = null,
        productName = "Chips",
        rating = 5,
        comment = "",
        advantages = "",
        disadvantages = "",
        isSaving = false,
    ),
    isPrimaryButtonLoading = false,
    isManualBackHandlerEnabled = true,
)
