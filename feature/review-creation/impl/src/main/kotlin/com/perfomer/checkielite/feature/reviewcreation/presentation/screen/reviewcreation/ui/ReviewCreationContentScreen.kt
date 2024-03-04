package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.productinfo.ProductInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo.ReviewInfoScreen
import com.perfomer.checkielite.navigation.voyager.BaseScreen

@OptIn(ExperimentalFoundationApi::class)
internal class ReviewCreationContentScreen(
    private val params: ReviewCreationParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewCreationStore>(params)) { state ->
        BackHandler { accept(OnBackPress) }

        val pagerState = rememberPagerState(
            initialPage = state.step,
            pageCount = { state.stepsCount },
        )

        ReviewCreationScreen(
            state = state,
            pagerState = pagerState,
            onPrimaryButtonClick = acceptable(OnPrimaryButtonClick),
            onBackPress = acceptable(OnBackPress),
        ) { pageIndex ->
            val reviewCreationPage by remember(pageIndex) {
                derivedStateOf { ReviewCreationPage.entries[pageIndex] }
            }

            when (reviewCreationPage) {
                ReviewCreationPage.PRODUCT_INFO -> ProductInfoScreen(
                    state = state.productInfoState,
                    onProductNameTextInput = acceptable(ProductInfo::OnProductNameTextInput),
                    onBrandTextInput = acceptable(ProductInfo::OnBrandTextInput),
                    onAddPictureClick = acceptable(ProductInfo.OnAddPictureClick),
                    onPictureClick = acceptable(ProductInfo::OnPictureClick),
                    onPictureDeleteClick = acceptable(ProductInfo::OnPictureDeleteClick),
                )

                ReviewCreationPage.REVIEW_INFO -> ReviewInfoScreen(
                    state = state.reviewInfoState,
                    onRatingSelect = acceptable(ReviewInfo::OnRatingSelect),
                    onReviewTextInput = acceptable(ReviewInfo::OnReviewTextInput),
                )
            }
        }

        UpdateEffect(state.step) {
            pagerState.animateScrollToPage(state.step)
        }
    }
}