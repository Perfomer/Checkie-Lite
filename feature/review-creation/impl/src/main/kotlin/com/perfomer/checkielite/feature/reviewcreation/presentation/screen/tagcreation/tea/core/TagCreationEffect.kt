package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text

internal sealed interface TagCreationEffect {

    data object FocusTagValueField : TagCreationEffect

    class ShowToast(
        val text: Text,
        val style: ToastStyle,
    ) : TagCreationEffect

    data object CollapseTagValueField : TagCreationEffect

    data object VibrateError : TagCreationEffect

    data object ShowTagDeleteConfirmationDialog : TagCreationEffect

    data object ShowExitConfirmationDialog : TagCreationEffect
}