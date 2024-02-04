package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.productinfo.ProductInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo.ReviewInfoScreen
import com.perfomer.checkielite.navigation.voyager.BaseScreen

@OptIn(ExperimentalFoundationApi::class)
internal class ReviewCreationContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewCreationStore>()) { state ->
        val pagerState = rememberPagerState(pageCount = { state.stepsCount })

        UpdateEffect(state.step) {
            pagerState.animateScrollToPage(state.step - 1)
        }

        ReviewCreationScreen(
            state = state,
            pagerState = pagerState,
            onPrimaryButtonClick = acceptable(OnPrimaryButtonClick),
            onBackPress = acceptable(OnBackPress),
        ) {
            when (state.currentPage) {
                ReviewCreationPage.PRODUCT_INFO -> ProductInfoScreen()
                ReviewCreationPage.REVIEW_INFO -> ReviewInfoScreen()
            }
        }
    }
}