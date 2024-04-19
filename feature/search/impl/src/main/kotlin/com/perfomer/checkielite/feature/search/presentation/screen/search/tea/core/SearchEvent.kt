package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

internal sealed interface SearchEvent

internal sealed interface SearchUiEvent : SearchEvent {

    data object OnBackPress : SearchUiEvent
}

internal sealed interface SearchNavigationEvent : SearchEvent