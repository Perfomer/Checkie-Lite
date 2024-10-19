package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

internal sealed interface ReviewDetailsEffect {

    data object ShowConfirmDeleteDialog : ReviewDetailsEffect

    sealed interface ShowToast : ReviewDetailsEffect {
        data object Syncing : ReviewDetailsEffect
        data object Deleted : ReviewDetailsEffect
    }
}