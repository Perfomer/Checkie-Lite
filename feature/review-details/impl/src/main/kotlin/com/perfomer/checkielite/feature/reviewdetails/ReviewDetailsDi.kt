package com.perfomer.checkielite.feature.reviewdetails

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.reviewdetails.data.repository.ReviewDetailsRepositoryImpl
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewDetailsRepository
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsReducer
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsStore
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.DeleteReviewActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.LoadReviewActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.ReviewDetailsNavigationActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.ReviewDetailsContentScreen
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val reviewDetailsModules
    get() = listOf(presentationModule, dataModule)

private val dataModule = module {
    singleOf(::ReviewDetailsRepositoryImpl) bind ReviewDetailsRepository::class
}

private val presentationModule = module {
    navigation {
        associate<ReviewDetailsDestination, ReviewDetailsContentScreen>()
    }

    factoryOf(::createReviewDetailsStore)
    factoryOf(::ReviewDetailsContentScreen)
}

internal fun createReviewDetailsStore(
    componentContext: ComponentContext,
    destination: ReviewDetailsDestination,
    reviewDetailsRepository: ReviewDetailsRepository,
    router: Router,
): ReviewDetailsStore {
    return ReviewDetailsStore(
        componentContext = componentContext,
        destination = destination,
        reducer = ReviewDetailsReducer(),
        uiStateMapper = ReviewDetailsUiStateMapper(),
        actors = setOf(
            ReviewDetailsNavigationActor(router),
            LoadReviewActor(reviewDetailsRepository),
            DeleteReviewActor(reviewDetailsRepository),
        ),
    )
}
