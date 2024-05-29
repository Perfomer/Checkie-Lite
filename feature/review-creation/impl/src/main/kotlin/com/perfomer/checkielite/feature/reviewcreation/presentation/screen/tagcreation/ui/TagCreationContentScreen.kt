package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.TagCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.FocusTagValueField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowErrorToast
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowTagDeleteConfirmationDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteTagClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnTagValueInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class TagCreationContentScreen(
    private val params: TagCreationParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<TagCreationStore>(params)) { state ->
        val focusRequester = remember { FocusRequester() }

        BackHandlerWithLifecycle { accept(OnBackPress) }

        EffectHandler { effect ->
            when (effect) {
                is ShowErrorToast.DeletionFailed -> Unit // todo
                is ShowErrorToast.SavingFailed -> Unit // todo
                is ShowTagDeleteConfirmationDialog -> Unit // todo
                is FocusTagValueField -> focusRequester.requestFocus()
            }
        }

        TagCreationScreen(
            state = state,
            focusRequester = focusRequester,
            onTagValueInput = acceptable(::OnTagValueInput),
            onDoneClick = acceptable(OnDoneClick),
            onDeleteTagClick = acceptable(OnDeleteTagClick),
        )
    }
}