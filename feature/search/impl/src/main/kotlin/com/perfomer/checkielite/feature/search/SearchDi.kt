package com.perfomer.checkielite.feature.search

import android.content.Context
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SortScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsParams
import com.perfomer.checkielite.feature.search.presentation.navigation.TagsScreenProvider
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
    factoryOf(::createSearchStore)
    factory { SearchScreenProvider(::SearchContentScreen) }

    factoryOf(::createSortStore)
    factory { SortScreenProvider(::SortContentScreen) }

    factoryOf(::createTagsStore)
    factory { TagsScreenProvider(::TagsContentScreen) }
}

internal fun createSearchStore(
    context: Context,
    params: SearchParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
    sortScreenProvider: SortScreenProvider,
    tagsScreenProvider: TagsScreenProvider,
): SearchStore {
    return SearchStore(
        params = params,
        reducer = SearchReducer(),
        uiStateMapper = SearchUiStateMapper(context),
        actors = setOf(
            SearchNavigationActor(router, sortScreenProvider, tagsScreenProvider),
            FilterReviewsActor(),
            SearchLoadTagsActor(localDataSource),
            LoadReviewsActor(localDataSource),
            LoadRecentSearchesActor(localDataSource),
            RememberRecentSearchActor(localDataSource),
            ClearRecentSearchesActor(localDataSource),
        ),
    )
}

internal fun createSortStore(
    context: Context,
    params: SortParams,
    router: Router,
): SortStore {
    return SortStore(
        params = params,
        reducer = SortReducer(),
        uiStateMapper = SortUiStateMapper(context),
        actors = setOf(
            SortNavigationActor(router),
        ),
    )
}

internal fun createTagsStore(
    context: Context,
    params: TagsParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
): TagsStore {
    return TagsStore(
        params = params,
        reducer = TagsReducer(),
        uiStateMapper = TagsUiStateMapper(context),
        actors = setOf(
            TagsNavigationActor(router),
            TagsLoadTagsActor(localDataSource),
        ),
    )
}