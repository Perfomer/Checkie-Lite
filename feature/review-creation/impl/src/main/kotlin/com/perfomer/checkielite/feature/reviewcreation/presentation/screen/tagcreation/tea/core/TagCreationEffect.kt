package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

internal sealed interface TagCreationEffect {

    data object FocusTagValueField : TagCreationEffect

    sealed interface ShowErrorToast : TagCreationEffect {
        data object DeletionFailed : ShowErrorToast
        data object SavingFailed : ShowErrorToast
    }

    data object CollapseTagValueField : TagCreationEffect

    data object VibrateError : TagCreationEffect

    data object ShowTagDeleteConfirmationDialog : TagCreationEffect

    data object ShowExitConfirmationDialog : TagCreationEffect
}