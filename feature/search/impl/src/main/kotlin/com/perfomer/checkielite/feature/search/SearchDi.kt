package com.perfomer.checkielite.feature.search

import android.content.Context
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.search.navigation.SearchParams
import com.perfomer.checkielite.feature.search.navigation.SearchScreenProvider
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.navigation.SortScreenProvider
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchReducer
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchStore
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.actor.SearchNavigationActor
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
}

internal fun createSearchStore(
    context: Context,
    params: SearchParams,
    router: Router,
): SearchStore {
    return SearchStore(
        params = params,
        reducer = SearchReducer(),
        uiStateMapper = SearchUiStateMapper(context),
        actors = setOf(
            SearchNavigationActor(router),
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