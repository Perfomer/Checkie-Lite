package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.TagCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowErrorToast
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteTagClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnEmojiSelect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnSelectedEmojiClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnTagValueInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class TagCreationContentScreen(
    private val params: TagCreationParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<TagCreationStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        EffectHandler { effect ->
            when (effect) {
                ShowErrorToast.DeletionFailed -> Unit // todo
                ShowErrorToast.SavingFailed -> Unit // todo
                ShowErrorToast.TagValueIsEmpty -> Unit // todo
            }
        }

        TagCreationScreen(
            state = state,
            onSelectedEmojiClick = acceptable(OnSelectedEmojiClick),
            onTagValueInput = acceptable(::OnTagValueInput),
            onEmojiSelect = acceptable(::OnEmojiSelect),
            onDoneClick = acceptable(OnDoneClick),
            onDeleteTagClick = acceptable(OnDeleteTagClick),
        )
    }
}