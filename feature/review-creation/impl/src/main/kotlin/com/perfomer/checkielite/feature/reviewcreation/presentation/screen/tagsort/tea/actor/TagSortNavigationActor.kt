package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core.TagSortNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class TagSortNavigationActor(
    private val router: Router,
) : Actor<TagSortCommand, TagSortEvent> {

    override fun act(commands: Flow<TagSortCommand>): Flow<TagSortEvent> {
        return commands.filterIsInstance<TagSortNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: TagSortNavigationCommand): TagSortNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
            is ExitWithResult -> exitWithResult(command.result)
        }

        return null
    }
}