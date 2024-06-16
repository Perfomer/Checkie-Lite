package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.core

internal sealed interface CurrencySelectorEffect {

    data object FocusTagValueField : CurrencySelectorEffect

    sealed interface ShowErrorToast : CurrencySelectorEffect {
        data object DeletionFailed : ShowErrorToast
        data object SavingFailed : ShowErrorToast
    }

    data object ShowTagDeleteConfirmationDialog : CurrencySelectorEffect
}