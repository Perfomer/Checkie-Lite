package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortUiEvent.OnSortingOptionClick

internal class TagSortReducer : DslReducer<TagSortCommand, TagSortEffect, TagSortEvent, TagSortState>() {

    override fun reduce(event: TagSortEvent) = when (event) {
        is TagSortUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: TagSortUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnDoneClick -> commands(ExitWithResult(TagSortResult.Success(state.currentOption)))
        is OnSortingOptionClick -> state { copy(currentOption = event.type) }
    }
}