package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.main.data.repository.ReviewsRepositoryImpl
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.feature.main.presentation.tea.MainReducer
import com.perfomer.checkielite.feature.main.presentation.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.tea.actor.LoadReviewsActor
import com.perfomer.checkielite.feature.main.presentation.tea.actor.MainNavigationActor
import com.perfomer.checkielite.feature.main.presentation.ui.MainContentScreen
import com.perfomer.checkielite.feature.main.presentation.ui.state.MainUiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
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
    factoryOf(::createMainStore)
    factory { MainScreenProvider { MainContentScreen() } }
}

internal fun createMainStore(
    reviewsRepository: ReviewsRepository,
    router: Router,
    reviewCreationScreenProvider: ReviewCreationScreenProvider,
    reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
): MainStore {
    return MainStore(
        reducer = MainReducer(),
        uiStateMapper = MainUiStateMapper(),
        actors = setOf(
            MainNavigationActor(router, reviewCreationScreenProvider, reviewDetailsScreenProvider),
            LoadReviewsActor(reviewsRepository),
        ),
    )
}