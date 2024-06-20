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
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortParams
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortScreenProvider
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.CurrencySelectorReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.CurrencySelectorStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.actor.CurrencySelectorNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.actor.FilterCurrenciesActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.tea.actor.LoadCurrenciesActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.CurrencySelectorContentScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.currencyselector.ui.state.CurrencySelectorUiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.ReviewCreationStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.CreateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadLatestCurrencyActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadLatestTagSortStrategyActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.RememberTagSortStrategyActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.ReviewCreationNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.SearchBrandsActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.UpdateReviewActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.WarmUpCurrenciesActor
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
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.TagSortReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.TagSortStore
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.actor.TagSortNavigationActor
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.TagSortContentScreen
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.ui.state.TagSortUiStateMapper
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

    factoryOf(::createTagSortStore)
    factory { TagSortScreenProvider(::TagSortContentScreen) }

    factoryOf(::createCurrencySelectorStore)
    factory { CurrencySelectorScreenProvider(::CurrencySelectorContentScreen) }
}

internal fun createReviewCreationStore(
    context: Context,
    params: ReviewCreationParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
    externalRouter: ExternalRouter,
    galleryScreenProvider: GalleryScreenProvider,
    tagCreationScreenProvider: TagCreationScreenProvider,
    currencySelectorScreenProvider: CurrencySelectorScreenProvider,
    tagSortScreenProvider: TagSortScreenProvider,
): ReviewCreationStore {
    return ReviewCreationStore(
        params = params,
        reducer = ReviewCreationReducer(),
        uiStateMapper = ReviewCreationUiStateMapper(context),
        actors = setOf(
            ReviewCreationNavigationActor(
                router = router,
                externalRouter = externalRouter,
                galleryScreenProvider = galleryScreenProvider,
                tagCreationScreenProvider = tagCreationScreenProvider,
                tagSortScreenProvider = tagSortScreenProvider,
                currencySelectorScreenProvider = currencySelectorScreenProvider,
            ),
            CreateReviewActor(localDataSource),
            UpdateReviewActor(localDataSource),
            LoadReviewActor(localDataSource),
            LoadTagsActor(localDataSource),
            SearchBrandsActor(localDataSource),
            WarmUpCurrenciesActor(localDataSource),
            LoadLatestCurrencyActor(localDataSource),
            LoadLatestTagSortStrategyActor(localDataSource),
            RememberTagSortStrategyActor(localDataSource),
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

internal fun createTagSortStore(
    context: Context,
    params: TagSortParams,
    router: Router,
): TagSortStore {
    return TagSortStore(
        params = params,
        reducer = TagSortReducer(),
        uiStateMapper = TagSortUiStateMapper(context),
        actors = setOf(
            TagSortNavigationActor(router),
        )
    )
}

internal fun createCurrencySelectorStore(
    context: Context,
    params: CurrencySelectorParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
): CurrencySelectorStore {
    return CurrencySelectorStore(
        params = params,
        reducer = CurrencySelectorReducer(),
        uiStateMapper = CurrencySelectorUiStateMapper(context),
        actors = setOf(
            CurrencySelectorNavigationActor(router),
            LoadCurrenciesActor(localDataSource),
            FilterCurrenciesActor(),
        )
    )
}