package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core

import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagSortingStrategy

internal sealed interface TagSortEvent

internal sealed interface TagSortUiEvent : TagSortEvent {

    data object OnBackPress : TagSortUiEvent

    data object OnDoneClick : TagSortUiEvent

    class OnSortingOptionClick(val type: TagSortingStrategy) : TagSortUiEvent
}

internal sealed interface TagSortNavigationEvent : TagSortEvent