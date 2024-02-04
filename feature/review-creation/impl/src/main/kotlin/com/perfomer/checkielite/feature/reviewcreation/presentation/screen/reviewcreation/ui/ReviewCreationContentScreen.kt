package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

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
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.productinfo.ProductInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.*
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewinfo.ReviewInfoScreen
import com.perfomer.checkielite.navigation.voyager.BaseScreen
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalFoundationApi::class)
internal class ReviewCreationContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewCreationStore>()) { state ->
        val pagerState = rememberPagerState(pageCount = { state.stepsCount })

        ReviewCreationScreen(
            state = state,
            pagerState = pagerState,
            onPrimaryButtonClick = acceptable(OnPrimaryButtonClick),
            onBackPress = acceptable(OnBackPress),
        ) { pageIndex ->
            val reviewCreationPage by remember(pageIndex) {
                derivedStateOf { enumValues<ReviewCreationPage>()[pageIndex] }
            }

            when (reviewCreationPage) {
                ReviewCreationPage.PRODUCT_INFO -> ProductInfoScreen(
                    state = state.productInfoState,
                    onProductNameTextInput = acceptable(ProductInfo::OnProductNameTextInput),
                    onBrandTextInput = acceptable(ProductInfo::OnBrandTextInput),
                    onAddPictureClick = acceptable(ProductInfo.OnAddPictureClick),
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
            coroutineScope { pagerState.animateScrollToPage(state.step - 1) }
        }
    }
}