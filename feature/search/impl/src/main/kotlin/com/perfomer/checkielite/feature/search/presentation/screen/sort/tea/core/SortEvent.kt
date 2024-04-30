package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy

internal sealed interface SortEvent

internal sealed interface SortUiEvent : SortEvent {

    data object OnBackPress : SortUiEvent

    data object OnDoneClick : SortUiEvent

    data object OnSortingOrderClick : SortUiEvent

    class OnSortingOptionClick(val type: ReviewsSortingStrategy) : SortUiEvent
}

internal sealed interface SortNavigationEvent : SortEvent