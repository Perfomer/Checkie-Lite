package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsStore
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnConfirmDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEditClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyImageClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyReviewTextClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureSelect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnStart
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class ReviewDetailsContentScreen(
    private val params: ReviewDetailsParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewDetailsStore>(params)) { state ->
        var isConfirmDeleteDialogShown by remember { mutableStateOf(false) }

        BackHandler { accept(OnBackPress) }

        LifecycleEffect(
            onStarted = acceptable(OnStart),
        )

        EffectHandler { effect ->
            when (effect) {
                ShowConfirmDeleteDialog -> isConfirmDeleteDialogShown = true
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
            onEmptyImageClick = acceptable(OnEmptyImageClick),
            onEmptyReviewTextClick = acceptable(OnEmptyReviewTextClick),
        )
    }
}