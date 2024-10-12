package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

internal sealed interface ReviewCreationEffect {

    data object ShowConfirmExitDialog : ReviewCreationEffect

    data object ShowErrorDialog : ReviewCreationEffect

    data object CloseKeyboard : ReviewCreationEffect

    data object FocusPriceField : ReviewCreationEffect

    data object FocusCommentField : ReviewCreationEffect
}