package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeConfig
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.VibratorPattern
import com.perfomer.checkielite.common.ui.util.rememberVibrator
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.common.ui.util.vibrateCompat
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.CloseKeyboard
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.CollapseProductNameField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.FocusCommentField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.FocusPriceField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.VibrateError
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnConfirmExitClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.Tags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.reviewinfo.ReviewInfoScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.tags.TagsScreen
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class ReviewCreationContentScreen(
    private val params: ReviewCreationParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewCreationStore>(params)) { state ->
        val focusManager = LocalFocusManager.current
        val priceFocusRequester = remember { FocusRequester() }
        val commentFocusRequester = remember { FocusRequester() }
        val productNameShakeController = rememberShakeController()
        val vibrator = rememberVibrator()

        var isConfirmExitDialogShown by remember { mutableStateOf(false) }
        var isErrorDialogShown by remember { mutableStateOf(false) }

        BackHandlerWithLifecycle { accept(OnBackPress) }

        EffectHandler { effect ->
            when (effect) {
                is ShowConfirmExitDialog -> isConfirmExitDialogShown = true
                is ShowErrorDialog -> isErrorDialogShown = true
                is CloseKeyboard -> focusManager.clearFocus()
                is FocusCommentField -> commentFocusRequester.requestFocus()
                is FocusPriceField -> priceFocusRequester.requestFocus()
                is CollapseProductNameField -> productNameShakeController.shake(ShakeConfig.inputError)
                is VibrateError -> vibrator.vibrateCompat(VibratorPattern.ERROR)
            }
        }

        val pagerState = rememberPagerState(
            initialPage = state.step,
            pageCount = { state.stepsCount },
        )

        val productInfoScrollState = rememberScrollState()
        val tagsScrollState = rememberScrollState()
        val reviewInfoScrollState = rememberScrollState()

        val currentPageScrollState by remember(pagerState.currentPage) {
            derivedStateOf {
                val currentPage = ReviewCreationPage.entries[pagerState.currentPage]

                when (currentPage) {
                    ReviewCreationPage.PRODUCT_INFO -> productInfoScrollState
                    ReviewCreationPage.TAGS -> tagsScrollState
                    ReviewCreationPage.REVIEW_INFO -> reviewInfoScrollState
                }
            }
        }

        ReviewCreationScreen(
            state = state,
            pagerState = pagerState,
            currentPageScrollState = currentPageScrollState,
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
                    scrollState = productInfoScrollState,
                    priceFocusRequester = priceFocusRequester,
                    productNameShakeController = productNameShakeController,
                    onProductNameTextInput = acceptable(ProductInfo::OnProductNameTextInput),
                    onBrandTextInput = acceptable(ProductInfo::OnBrandTextInput),
                    onPriceTextInput = acceptable(ProductInfo::OnPriceTextInput),
                    onPriceCurrencyClick = acceptable(ProductInfo.OnPriceCurrencyClick),
                    onAddPictureClick = acceptable(ProductInfo.OnAddPictureClick),
                    onPictureClick = acceptable(ProductInfo::OnPictureClick),
                    onPictureDeleteClick = acceptable(ProductInfo::OnPictureDeleteClick),
                    onPictureReorder = acceptable(ProductInfo::OnPictureReorder),
                )

                ReviewCreationPage.TAGS -> TagsScreen(
                    state = state.tagsState,
                    scrollState = tagsScrollState,
                    onTagSortClick = acceptable(Tags.OnTagSortClick),
                    onCreateTagClick = acceptable(Tags.OnCreateTagClick),
                    onTagClick = acceptable(Tags::OnTagClick),
                    onTagLongClick = acceptable(Tags::OnTagLongClick),
                    onSearchQueryInput = acceptable(Tags::OnSearchQueryInput),
                    onSearchQueryClearClick = acceptable(Tags.OnSearchQueryClearClick),
                )

                ReviewCreationPage.REVIEW_INFO -> ReviewInfoScreen(
                    state = state.reviewInfoState,
                    scrollState = reviewInfoScrollState,
                    commentFocusRequester = commentFocusRequester,
                    onRatingSelect = acceptable(ReviewInfo::OnRatingSelect),
                    onCommentInput = acceptable(ReviewInfo::OnCommentInput),
                    onAdvantagesInput = acceptable(ReviewInfo::OnAdvantagesInput),
                    onDisadvantagesInput = acceptable(ReviewInfo::OnDisadvantagesInput),
                )
            }
        }

        UpdateEffect(state.step) {
            pagerState.animateScrollToPage(state.step)
        }
    }
}