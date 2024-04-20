package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnBackPress

internal class TagCreationReducer : DslReducer<TagCreationCommand, TagCreationEffect, TagCreationEvent, TagCreationState>() {

    override fun reduce(event: TagCreationEvent) = when (event) {
        is TagCreationUiEvent -> reduceUi(event)
        is Initialize -> Unit
    }

    private fun reduceUi(event: TagCreationUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
    }
}