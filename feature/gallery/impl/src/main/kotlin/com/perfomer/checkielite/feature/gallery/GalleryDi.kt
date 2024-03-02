package com.perfomer.checkielite.feature.gallery

import android.content.Context
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryParams
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
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
    factoryOf(::createGalleryStore)
    factory { GalleryScreenProvider(::GalleryContentScreen) }
}

internal fun createGalleryStore(
    context: Context,
    params: GalleryParams,
    router: Router,
): GalleryStore {
    return GalleryStore(
        params = params,
        reducer = GalleryReducer(),
        uiStateMapper = GalleryUiStateMapper(context),
        actors = setOf(
            GalleryNavigationActor(router),
        ),
    )
}