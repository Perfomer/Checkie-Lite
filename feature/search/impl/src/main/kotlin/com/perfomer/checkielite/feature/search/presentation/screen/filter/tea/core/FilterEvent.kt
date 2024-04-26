package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core

internal sealed interface FilterEvent

internal sealed interface FilterUiEvent : FilterEvent {

    data object OnBackPress : FilterUiEvent

    data object OnDoneClick : FilterUiEvent
}

internal sealed interface FilterNavigationEvent : FilterEvent