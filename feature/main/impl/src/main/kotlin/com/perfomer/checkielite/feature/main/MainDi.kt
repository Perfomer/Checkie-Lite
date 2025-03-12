package com.perfomer.checkielite.feature.main

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.repository.TagRepository
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.main.data.repository.ReviewsRepositoryImpl
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import com.perfomer.checkielite.feature.main.navigation.MainDestination
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainReducer
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadReviewsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.MainNavigationActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.MainContentScreen
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModules
    get() = listOf(presentationModule, dataModule)

private val dataModule = module {
    singleOf(::ReviewsRepositoryImpl) bind ReviewsRepository::class
}

private val presentationModule = module {
    navigation { associate<MainDestination, MainContentScreen>() }

    factoryOf(::MainContentScreen)
    factoryOf(::createMainStore)
}

internal fun createMainStore(
    componentContext: ComponentContext,
    reviewsRepository: ReviewsRepository,
    tagRepository: TagRepository,
    router: Router,
): MainStore {
    return MainStore(
        componentContext = componentContext,
        reducer = MainReducer(),
        uiStateMapper = MainUiStateMapper(),
        actors = setOf(
            MainNavigationActor(router = router),
            LoadReviewsActor(reviewsRepository),
            LoadTagsActor(tagRepository),
        ),
    )
}