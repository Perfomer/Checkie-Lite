package com.perfomer.checkielite.feature.reviewcreation

import android.content.Context
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.data.repository.ReviewCreationRepositoryImpl
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.ReviewCreationRepository
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.CreateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.ReviewCreationNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.SearchBrandsActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.UpdateReviewActor
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
    context: Context,
    params: ReviewCreationParams,
    reviewCreationRepository: ReviewCreationRepository,
    router: Router,
    externalRouter: ExternalRouter,
    galleryScreenProvider: GalleryScreenProvider,
) : ReviewCreationStore{
    return ReviewCreationStore(
        params = params,
        reducer = ReviewCreationReducer(),
        uiStateMapper = ReviewCreationUiStateMapper(context),
        actors = setOf(
            ReviewCreationNavigationActor(router, externalRouter, galleryScreenProvider),
            CreateReviewActor(reviewCreationRepository),
            UpdateReviewActor(reviewCreationRepository),
            LoadReviewActor(reviewCreationRepository),
            SearchBrandsActor(reviewCreationRepository),
        )
    )
}