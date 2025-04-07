package com.perfomer.checkielite.feature.main

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainReducer
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadReviewsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.MainNavigationActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.MainContentScreen
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModules
    get() = listOf(presentationModule)


private val presentationModule = module {
    navigation { associate<MainDestination, MainContentScreen>() }

    factoryOf(::MainContentScreen)
    factoryOf(::createMainStore)
}

internal fun createMainStore(
    componentContext: ComponentContext,
    reviewRepository: ReviewRepository,
    tagRepository: TagRepository,
    router: Router,
): MainStore {
    return MainStore(
        componentContext = componentContext,
        reducer = MainReducer(),
        uiStateMapper = MainUiStateMapper(),
        actors = setOf(
            MainNavigationActor(router),
            LoadReviewsActor(reviewRepository),
            LoadTagsActor(tagRepository),
        ),
    )
}