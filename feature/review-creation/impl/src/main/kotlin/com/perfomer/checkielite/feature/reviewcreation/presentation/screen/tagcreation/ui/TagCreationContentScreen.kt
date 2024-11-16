package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.modifier.ShakeConfig
import com.perfomer.checkielite.common.ui.cui.modifier.rememberShakeController
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalCuiToastHostState
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberErrorToast
import com.perfomer.checkielite.common.ui.util.VibratorPattern
import com.perfomer.checkielite.common.ui.util.rememberVibrator
import com.perfomer.checkielite.common.ui.util.vibrateCompat
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.TagCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.CollapseTagValueField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.FocusTagValueField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowErrorToast
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowTagDeleteConfirmationDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.VibrateError
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteConfirmClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteTagClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnEmojiSelect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnSelectedEmojiClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnTagValueInput

internal class TagCreationContentScreen(
    private val store: TagCreationStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        val tagValueFocusRequester = remember { FocusRequester() }
        val vibrator = rememberVibrator()
        val tagValueShakeController = rememberShakeController()

        BackHandler { accept(OnBackPress) }

        val toastHostState = LocalCuiToastHostState.current
        val deleteErrorToast = rememberErrorToast(R.string.tagcreation_error_delete)
        val saveErrorToast = rememberErrorToast(R.string.tagcreation_error_save)

        var isConfirmDeleteDialogShown by remember { mutableStateOf(false) }

        EffectHandler { effect ->
            when (effect) {
                is ShowErrorToast.DeletionFailed -> toastHostState.showToast(deleteErrorToast)
                is ShowErrorToast.SavingFailed -> toastHostState.showToast(saveErrorToast)
                is ShowTagDeleteConfirmationDialog -> isConfirmDeleteDialogShown = true
                is FocusTagValueField -> tagValueFocusRequester.requestFocus()
                is CollapseTagValueField -> tagValueShakeController.shake(ShakeConfig.inputError)
                is VibrateError -> vibrator.vibrateCompat(VibratorPattern.ERROR)
            }
        }

        TagCreationScreen(
            state = state,

            tagValueFocusRequester = tagValueFocusRequester,
            tagValueShakeController = tagValueShakeController,

            isConfirmDeleteDialogShown = isConfirmDeleteDialogShown,
            onDeleteDialogDismiss = { isConfirmDeleteDialogShown = false },
            onDeleteDialogConfirm = acceptable(OnDeleteConfirmClick),

            onTagValueInput = acceptable(::OnTagValueInput),
            onSelectedEmojiClick = acceptable(OnSelectedEmojiClick),
            onEmojiSelect = acceptable(::OnEmojiSelect),
            onDoneClick = acceptable(OnDoneClick),
            onDeleteTagClick = acceptable(OnDeleteTagClick)
        )
    }
}