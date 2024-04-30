package com.perfomer.checkielite.feature.search

import android.content.Context
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterParams
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SortScreenProvider
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.FilterReducer
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.FilterStore
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.actor.FilterNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.actor.LoadTagsActor
import com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.FilterContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.filter.ui.state.FilterUiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchReducer
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchStore
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.ClearRecentSearchesActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.LoadRecentSearchesActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.RememberRecentSearchActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.SearchNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.SearchReviewsActor
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.SearchContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.SearchUiStateMapper
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortReducer
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortStore
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.actor.SortNavigationActor
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.SortContentScreen
import com.perfomer.checkielite.feature.search.presentation.screen.sort.ui.state.SortUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchModules
    get() = listOf(presentationModule)

private val presentationModule = module {
    factoryOf(::createSearchStore)
    factory { SearchScreenProvider(::SearchContentScreen) }

    factoryOf(::createSortStore)
    factory { SortScreenProvider(::SortContentScreen) }

    factoryOf(::createFilterStore)
    factory { FilterScreenProvider(::FilterContentScreen) }
}

internal fun createSearchStore(
    context: Context,
    params: SearchParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
    reviewDetailsScreenProvider: ReviewDetailsScreenProvider,
    sortScreenProvider: SortScreenProvider,
    filterScreenProvider: FilterScreenProvider,
): SearchStore {
    return SearchStore(
        params = params,
        reducer = SearchReducer(),
        uiStateMapper = SearchUiStateMapper(context),
        actors = setOf(
            SearchNavigationActor(router, reviewDetailsScreenProvider, sortScreenProvider, filterScreenProvider),
            SearchReviewsActor(localDataSource),
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

internal fun createFilterStore(
    context: Context,
    params: FilterParams,
    localDataSource: CheckieLocalDataSource,
    router: Router,
): FilterStore {
    return FilterStore(
        params = params,
        reducer = FilterReducer(),
        uiStateMapper = FilterUiStateMapper(context),
        actors = setOf(
            FilterNavigationActor(router),
            LoadTagsActor(localDataSource),
        ),
    )
}