package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

internal sealed interface TagCreationEvent {

    data object Initialize : TagCreationEvent
}

internal sealed interface TagCreationUiEvent : TagCreationEvent {

    data object OnBackPress : TagCreationUiEvent
}

internal sealed interface TagCreationNavigationEvent : TagCreationEvent