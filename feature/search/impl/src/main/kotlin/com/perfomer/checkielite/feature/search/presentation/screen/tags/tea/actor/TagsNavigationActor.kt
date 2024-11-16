package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsEvent
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationCommand
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core.TagsNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class TagsNavigationActor(
    private val router: Router,
) : Actor<TagsCommand, TagsEvent> {

    override fun act(commands: Flow<TagsCommand>): Flow<TagsEvent> {
        return commands.filterIsInstance<TagsNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: TagsNavigationCommand): TagsNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
            is ExitWithResult -> exitWithResult(command.result)
        }

        return null
    }
}