package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.CommonString
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberToast
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberWarningToast
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewdetails.R
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsStore
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowToast
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnAddTagsClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnConfirmDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEditClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyImageClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyPriceClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyReviewTextClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureSelect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnRecommendationClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnStart
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnTagClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class ReviewDetailsContentScreen(
    private val params: ReviewDetailsParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewDetailsStore>(params)) { state ->
        val toastHost = LocalCuiToastHostState.current
        var isConfirmDeleteDialogShown by remember { mutableStateOf(false) }

        val syncingToast = rememberWarningToast(CommonString.common_toast_syncing)
        val deletedToast = rememberToast(R.string.reviewdetails_toast_deleted)

        BackHandlerWithLifecycle { accept(OnBackPress) }

        LifecycleEffect(
            onStarted = acceptable(OnStart),
        )

        EffectHandler { effect ->
            when (effect) {
                is ShowConfirmDeleteDialog -> isConfirmDeleteDialogShown = true
                is ShowToast.Syncing -> toastHost.showToast(syncingToast)
                is ShowToast.Deleted -> toastHost.showToast(deletedToast)
            }
        }

        ReviewDetailsScreen(
            state = state,
            showDeleteDialog = isConfirmDeleteDialogShown,
            onDeleteDialogDismiss = { isConfirmDeleteDialogShown = false },
            onDeleteDialogConfirm = acceptable(OnConfirmDeleteClick),
            onNavigationIconClick = acceptable(OnBackPress),
            onPageChange = acceptable(::OnPictureSelect),
            onEditClick = acceptable(OnEditClick),
            onDeleteClick = acceptable(OnDeleteClick),
            onPictureClick = acceptable(OnPictureClick),
            onEmptyImageClick = acceptable(OnEmptyImageClick),
            onEmptyPriceClick = acceptable(OnEmptyPriceClick),
            onEmptyReviewTextClick = acceptable(OnEmptyReviewTextClick),
            onAddTagsClick = acceptable(OnAddTagsClick),
            onTagClick = acceptable(::OnTagClick),
            onRecommendationClick = acceptable(::OnRecommendationClick),
        )
    }
}