package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

internal sealed interface TagCreationEffect {

    data object FocusTagValueField : TagCreationEffect

    sealed interface ShowErrorToast : TagCreationEffect {
        data object DeletionFailed : ShowErrorToast
        data object SavingFailed : ShowErrorToast
    }

    data object ShowTagDeleteConfirmationDialog : TagCreationEffect
}