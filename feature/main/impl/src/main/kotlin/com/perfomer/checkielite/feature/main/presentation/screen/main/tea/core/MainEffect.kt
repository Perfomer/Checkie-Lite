package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

internal sealed interface MainEffect {

    class ShowToast(val reason: Reason) : MainEffect {
        enum class Reason {
            REVIEW_CREATED,
        }
    }
}