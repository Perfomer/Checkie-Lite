package com.perfomer.checkielite.feature.main

import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.main.data.repository.ReviewsRepositoryImpl
import com.perfomer.checkielite.feature.main.domain.repository.ReviewsRepository
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainReducer
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadReviewsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.actor.MainNavigationActor
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.MainContentScreen
import com.perfomer.checkielite.feature.main.presentation.screen.main.ui.state.MainUiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchScreenProvider
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
    localDataSource: CheckieLocalDataSource,
    router: Router,
    reviewCreationScreenProvider: ReviewCreationScreenProvider,
    reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
    searchScreenProvider: SearchScreenProvider,
): MainStore {
    return MainStore(
        reducer = MainReducer(),
        uiStateMapper = MainUiStateMapper(),
        actors = setOf(
            MainNavigationActor(router, reviewCreationScreenProvider, reviewDetailsScreenProvider, searchScreenProvider),
            LoadReviewsActor(reviewsRepository),
            LoadTagsActor(localDataSource),
        ),
    )
}