package com.perfomer.checkielite.feature.gallery

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.gallery.navigation.GalleryDestination
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.GalleryReducer
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.GalleryStore
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.tea.actor.GalleryNavigationActor
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.GalleryContentScreen
import com.perfomer.checkielite.feature.gallery.presentation.screen.gallery.ui.state.GalleryUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val galleryModules
    get() = listOf(presentationModule)

private val presentationModule = module {
    navigation {
        associate<GalleryDestination, GalleryContentScreen>()
    }

    factoryOf(::createGalleryStore)
    factoryOf(::GalleryContentScreen)
}

internal fun createGalleryStore(
    componentContext: ComponentContext,
    destination: GalleryDestination,
    context: Context,
    router: Router,
): GalleryStore {
    return GalleryStore(
        componentContext = componentContext,
        destination = destination,
        reducer = GalleryReducer(),
        uiStateMapper = GalleryUiStateMapper(context),
        actors = setOf(
            GalleryNavigationActor(router),
        ),
    )
}