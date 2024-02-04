@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.button.CuiPrimaryButton
import com.perfomer.checkielite.common.ui.theme.CuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewInfoPageUiState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget.ProgressAppBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun ReviewCreationScreen(
    state: ReviewCreationUiState,
    pagerState: PagerState,
    onPrimaryButtonClick: () -> Unit = {},
    onBackPress: () -> Unit = {},
    content: @Composable PagerScope.(page: Int) -> Unit,
) {
    val backDrawableResource = remember(state.step) {
        if (state.step == 1) CommonDrawable.ic_cross
        else CommonDrawable.ic_arrow_back
    }

    Box(modifier = Modifier.background(CuiPalette.Light.BackgroundPrimary)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProgressAppBar(
                step = state.step,
                stepsCount = state.stepsCount,
                trailingIconPainter = painterResource(backDrawableResource),
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
            CuiPrimaryButton(
                text = state.primaryButtonText,
                onClick = onPrimaryButtonClick,
            )
        }
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
            primaryButtonText = "Next",
            productInfoState = ProductInfoPageUiState(
                productName = "",
                brand = "",
                picturesUri = emptyPersistentList(),
            ),
            reviewInfoState = ReviewInfoPageUiState(
                rating = 5,
                reviewText = "",
            )
        ),
        pagerState = rememberPagerState { 1 },
        content = {},
    )
}