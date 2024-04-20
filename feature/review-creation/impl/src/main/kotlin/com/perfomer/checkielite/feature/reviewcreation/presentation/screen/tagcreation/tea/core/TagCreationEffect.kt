package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

internal sealed interface TagCreationEffect {

    sealed interface ShowErrorToast : TagCreationEffect {
        data object TagValueIsEmpty : ShowErrorToast
        data object DeletionFailed : ShowErrorToast
        data object SavingFailed : ShowErrorToast
    }
}