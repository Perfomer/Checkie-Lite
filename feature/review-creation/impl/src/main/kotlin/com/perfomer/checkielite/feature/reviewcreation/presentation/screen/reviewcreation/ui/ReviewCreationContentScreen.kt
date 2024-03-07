package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnConfirmExitClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.reviewinfo.ReviewInfoScreen
import com.perfomer.checkielite.navigation.voyager.BaseScreen

@OptIn(ExperimentalFoundationApi::class)
internal class ReviewCreationContentScreen(
    private val params: ReviewCreationParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewCreationStore>(params)) { state ->
        var isConfirmExitDialogShown by remember { mutableStateOf(false) }
        var isErrorDialogShown by remember { mutableStateOf(false) }

        BackHandlerWithLifecycle { accept(OnBackPress) }

        EffectHandler { effect ->
            when (effect) {
                is ShowConfirmExitDialog -> isConfirmExitDialogShown = true
                is ShowErrorDialog -> isErrorDialogShown = true
            }
        }

        val pagerState = rememberPagerState(
            initialPage = state.step,
            pageCount = { state.stepsCount },
        )

        ReviewCreationScreen(
            state = state,
            pagerState = pagerState,

            showExitDialog = isConfirmExitDialogShown,
            onExitDialogDismiss = { isConfirmExitDialogShown = false },
            onExitDialogConfirm = acceptable(OnConfirmExitClick),

            showErrorDialog = isErrorDialogShown,
            onErrorDialogConfirm = acceptable(OnBackPress),

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
                    onPictureReorder = acceptable(ProductInfo::OnPictureReorder),
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