package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeNavigationCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class ThemeNavigationActor(
    private val router: Router,
) : Actor<ThemeCommand, ThemeEvent> {

    override fun act(commands: Flow<ThemeCommand>): Flow<ThemeEvent> {
        return commands.filterIsInstance<ThemeNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: ThemeNavigationCommand): ThemeNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
        }

        return null
    }
}