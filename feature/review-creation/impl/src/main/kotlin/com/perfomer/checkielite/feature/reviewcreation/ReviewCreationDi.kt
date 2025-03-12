package com.perfomer.checkielite.feature.reviewcreation

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.navigation.ExternalRouter
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.reviewcreation.data.repository.CheckieEmojiRepositoryImpl
import com.perfomer.checkielite.feature.reviewcreation.domain.repository.CheckieEmojiRepository
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.CurrencySelectorDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationDestination
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortDestination
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
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor.WarmUpEmojisActor
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
    navigation {
        associate<ReviewCreationDestination, ReviewCreationContentScreen>()
        associate<CurrencySelectorDestination, CurrencySelectorContentScreen>()
        associate<TagCreationDestination, TagCreationContentScreen>()
        associate<TagSortDestination, TagSortContentScreen>()
    }

    factoryOf(::createReviewCreationStore)
    factoryOf(::ReviewCreationContentScreen)

    factoryOf(::createTagCreationStore)
    factoryOf(::TagCreationContentScreen)

    factoryOf(::createTagSortStore)
    factoryOf(::TagSortContentScreen)

    factoryOf(::createCurrencySelectorStore)
    factoryOf(::CurrencySelectorContentScreen)
}

internal fun createReviewCreationStore(
    componentContext: ComponentContext,
    destination: ReviewCreationDestination,
    context: Context,
    localDataSource: CheckieLocalDataSource,
    reviewRepository: ReviewRepository,
    emojiRepository: CheckieEmojiRepository,
    router: Router,
    externalRouter: ExternalRouter,
): ReviewCreationStore {
    return ReviewCreationStore(
        componentContext = componentContext,
        destination = destination,
        reducer = ReviewCreationReducer(),
        uiStateMapper = ReviewCreationUiStateMapper(context),
        actors = setOf(
            ReviewCreationNavigationActor(router, externalRouter),
            CreateReviewActor(reviewRepository),
            UpdateReviewActor(reviewRepository),
            LoadReviewActor(reviewRepository),
            LoadTagsActor(localDataSource),
            SearchBrandsActor(localDataSource),
            WarmUpCurrenciesActor(localDataSource),
            WarmUpEmojisActor(emojiRepository),
            LoadLatestCurrencyActor(localDataSource),
            LoadLatestTagSortStrategyActor(localDataSource),
            RememberTagSortStrategyActor(localDataSource),
        )
    )
}

internal fun createTagCreationStore(
    componentContext: ComponentContext,
    destination: TagCreationDestination,
    context: Context,
    localDataSource: CheckieLocalDataSource,
    emojiRepository: CheckieEmojiRepository,
    router: Router,
): TagCreationStore {
    return TagCreationStore(
        componentContext = componentContext,
        destination = destination,
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
    componentContext: ComponentContext,
    destination: TagSortDestination,
    context: Context,
    router: Router,
): TagSortStore {
    return TagSortStore(
        componentContext = componentContext,
        destination = destination,
        reducer = TagSortReducer(),
        uiStateMapper = TagSortUiStateMapper(context),
        actors = setOf(
            TagSortNavigationActor(router),
        )
    )
}

internal fun createCurrencySelectorStore(
    componentContext: ComponentContext,
    destination: CurrencySelectorDestination,
    context: Context,
    localDataSource: CheckieLocalDataSource,
    router: Router,
): CurrencySelectorStore {
    return CurrencySelectorStore(
        componentContext = componentContext,
        destination = destination,
        reducer = CurrencySelectorReducer(),
        uiStateMapper = CurrencySelectorUiStateMapper(context),
        actors = setOf(
            CurrencySelectorNavigationActor(router),
            LoadCurrenciesActor(localDataSource),
            FilterCurrenciesActor(),
        )
    )
}