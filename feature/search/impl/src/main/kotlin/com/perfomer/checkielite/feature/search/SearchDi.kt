package com.perfomer.checkielite.feature.search

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.SortDestination
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsDestination
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchReducer
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchStore
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.ClearRecentSearchesActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.FilterReviewsActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.LoadRecentSearchesActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.LoadReviewsActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.RememberRecentSearchActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.SearchNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.SearchContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortReducer
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortStore
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.actor.SortNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.SortContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.TagsReducer
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.TagsStore
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.actor.TagsNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.TagsContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.tags.ui.state.TagsUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.LoadTagsActor as SearchLoadTagsActor
import com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.actor.LoadTagsActor as TagsLoadTagsActor

val searchModules
    get() = listOf(presentationModule)

private val presentationModule = module {
    navigation {
        associate<SearchDestination, SearchContentScreen>()
        associate<SortDestination, SortContentScreen>()
        associate<TagsDestination, TagsContentScreen>()
    }

    factoryOf(::createSearchStore)
    factoryOf(::SearchContentScreen)

    factoryOf(::createSortStore)
    factoryOf(::SortContentScreen)

    factoryOf(::createTagsStore)
    factoryOf(::TagsContentScreen)
}

internal fun createSearchStore(
    componentContext: ComponentContext,
    destination: SearchDestination,
    context: Context,
    localDataSource: CheckieLocalDataSource,
    reviewRepository: ReviewRepository,
    router: Router,
): SearchStore {
    return SearchStore(
        componentContext = componentContext,
        destination = destination,
        reducer = SearchReducer(),
        uiStateMapper = SearchUiStateMapper(context),
        actors = setOf(
            SearchNavigationActor(router),
            FilterReviewsActor(),
            SearchLoadTagsActor(localDataSource),
            LoadReviewsActor(reviewRepository),
            LoadRecentSearchesActor(localDataSource),
            RememberRecentSearchActor(localDataSource),
            ClearRecentSearchesActor(localDataSource),
        ),
    )
}

internal fun createSortStore(
    componentContext: ComponentContext,
    destination: SortDestination,
    context: Context,
    router: Router,
): SortStore {
    return SortStore(
        componentContext = componentContext,
        destination = destination,
        reducer = SortReducer(),
        uiStateMapper = SortUiStateMapper(context),
        actors = setOf(
            SortNavigationActor(router),
        ),
    )
}

internal fun createTagsStore(
    componentContext: ComponentContext,
    destination: TagsDestination,
    context: Context,
    localDataSource: CheckieLocalDataSource,
    router: Router,
): TagsStore {
    return TagsStore(
        componentContext = componentContext,
        destination = destination,
        reducer = TagsReducer(),
        uiStateMapper = TagsUiStateMapper(context),
        actors = setOf(
            TagsNavigationActor(router),
            TagsLoadTagsActor(localDataSource),
        ),
    )
}