package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

internal sealed interface SortEvent

internal sealed interface SortUiEvent : SortEvent {

    data object OnBackPress : SortUiEvent
}

internal sealed interface SortNavigationEvent : SortEvent