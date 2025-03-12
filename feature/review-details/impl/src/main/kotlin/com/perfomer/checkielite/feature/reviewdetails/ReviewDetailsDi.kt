package com.perfomer.checkielite.feature.reviewdetails

import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.repository.BrandRepository
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsReducer
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsStore
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.DeleteReviewActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.LoadReviewActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.actor.ReviewDetailsNavigationActor
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.ReviewDetailsContentScreen
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state.ReviewDetailsUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val reviewDetailsModules
    get() = listOf(presentationModule)

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
    reviewRepository: ReviewRepository,
    brandRepository: BrandRepository,
    router: Router,
): ReviewDetailsStore {
    return ReviewDetailsStore(
        componentContext = componentContext,
        destination = destination,
        reducer = ReviewDetailsReducer(),
        uiStateMapper = ReviewDetailsUiStateMapper(),
        actors = setOf(
            ReviewDetailsNavigationActor(router),
            LoadReviewActor(reviewRepository, brandRepository),
            DeleteReviewActor(reviewRepository),
        ),
    )
}
