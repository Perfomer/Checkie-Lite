package com.perfomer.checkielite.feature.reviewdetails

import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewdetails.data.repository.ReviewDetailsRepositoryImpl
import com.perfomer.checkielite.feature.reviewdetails.domain.repository.ReviewDetailsRepository
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
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
    factoryOf(::createReviewDetailsStore)
    factory { ReviewDetailsScreenProvider(::ReviewDetailsContentScreen) }
}

internal fun createReviewDetailsStore(
    params: ReviewDetailsParams,
    reviewDetailsRepository: ReviewDetailsRepository,
    router: Router,
    reviewCreationScreenProvider: ReviewCreationScreenProvider,
    galleryScreenProvider: GalleryScreenProvider,
): ReviewDetailsStore {
    return ReviewDetailsStore(
        params = params,
        reducer = ReviewDetailsReducer(),
        uiStateMapper = ReviewDetailsUiStateMapper(),
        actors = setOf(
            ReviewDetailsNavigationActor(router, reviewCreationScreenProvider, galleryScreenProvider),
            LoadReviewActor(reviewDetailsRepository),
            DeleteReviewActor(reviewDetailsRepository),
        ),
    )
}
