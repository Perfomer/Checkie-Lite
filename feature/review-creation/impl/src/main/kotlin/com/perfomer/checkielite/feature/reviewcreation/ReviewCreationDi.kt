package com.perfomer.checkielite.feature.reviewcreation

import android.content.Context
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.gallery.navigation.GalleryScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.data.repository.CheckieEmojiRepositoryImpl
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationParams
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.CreateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.ReviewCreationNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.SearchBrandsActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.UpdateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.ReviewCreationContentScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ReviewCreationUiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.TagCreationReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.TagCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.CreateTagActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.DeleteTagActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.LoadEmojisActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.LoadTagActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.TagCreationNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.UpdateTagActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor.ValidateTagNameActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.TagCreationContentScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.ui.state.TagCreationUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val reviewCreationModules
    get() = listOf(presentationModule, dataModule)

private val dataModule = module {
    singleOf(::CheckieEmojiRepositoryImpl) bind CheckieEmojiRepository::class
}

private val presentationModule = module {
    factoryOf(::createReviewCreationStore)
    factory { ReviewCreationScreenProvider(::ReviewCreationContentScreen) }

    factoryOf(::createTagCreationStore)
    factory { TagCreationScreenProvider(::TagCreationContentScreen) }
}

internal fun createReviewCreationStore(
    context: Context,
    params: ReviewCreationParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
    externalRouter: ExternalRouter,
    galleryScreenProvider: GalleryScreenProvider,
    tagCreationScreenProvider: TagCreationScreenProvider,
): ReviewCreationStore {
    return ReviewCreationStore(
        params = params,
        reducer = ReviewCreationReducer(),
        uiStateMapper = ReviewCreationUiStateMapper(context),
        actors = setOf(
            ReviewCreationNavigationActor(router, externalRouter, galleryScreenProvider, tagCreationScreenProvider),
            CreateReviewActor(localDataSource),
            UpdateReviewActor(localDataSource),
            LoadReviewActor(localDataSource),
            LoadTagsActor(localDataSource),
            SearchBrandsActor(localDataSource),
        )
    )
}

internal fun createTagCreationStore(
    context: Context,
    params: TagCreationParams,
    localDataSource: CheckieLocalDataSource,
    emojiRepository: CheckieEmojiRepository,
    router: Router,
): TagCreationStore {
    return TagCreationStore(
        params = params,
        reducer = TagCreationReducer(),
        uiStateMapper = TagCreationUiStateMapper(context),
        actors = setOf(
            TagCreationNavigationActor(router),
            LoadTagActor(localDataSource),
            ValidateTagNameActor(localDataSource),
            CreateTagActor(localDataSource),
            DeleteTagActor(localDataSource),
            UpdateTagActor(localDataSource),
            LoadEmojisActor(emojiRepository),
        )
    )
}