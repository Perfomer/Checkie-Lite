package com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryEvent
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationCommand
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationCommand.Exit
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.core.GalleryNavigationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class GalleryNavigationActor(
    private val router: Router,
) : Actor<GalleryCommand, GalleryEvent> {

    override fun act(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands.filterIsInstance<GalleryNavigationCommand>()
            .mapLatest(::handleCommand)
            .filterNotNull()
    }

    private fun handleCommand(command: GalleryNavigationCommand): GalleryNavigationEvent? = with(router) {
        when (command) {
            is Exit -> exit()
        }

        return null
    }
}