package com.perfomer.checkielite.feature.reviewcreation

import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.reviewcreation.data.repository.ReviewCreationRepositoryImpl
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.CreateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.ReviewCreationNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.ReviewCreationContentScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val reviewCreationModules
    get() = listOf(presentationModule, dataModule)

private val dataModule = module {
    singleOf(::ReviewCreationRepositoryImpl) bind ReviewCreationRepository::class
}

private val presentationModule  = module {
    factoryOf(::createReviewCreationStore)
    factory { ReviewCreationScreenProvider(::ReviewCreationContentScreen) }
}

internal fun createReviewCreationStore(
    reviewCreationRepository: ReviewCreationRepository,
    router: Router,
) : ReviewCreationStore{
    return ReviewCreationStore(
        reducer = ReviewCreationReducer(),
        uiStateMapper = ReviewCreationUiStateMapper(),
        actors = setOf(
            ReviewCreationNavigationActor(router),
            CreateReviewActor(reviewCreationRepository),
        )
    )
}