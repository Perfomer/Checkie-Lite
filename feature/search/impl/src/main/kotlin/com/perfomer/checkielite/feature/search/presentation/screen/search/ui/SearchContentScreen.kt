package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.search.presentation.navigation.SearchParams
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchStore
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnAllFiltersClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnFilterClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnRecentSearchesClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchFieldInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class SearchContentScreen(
    private val params: SearchParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<SearchStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        SearchScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
            onSearchFieldInput = acceptable(::OnSearchFieldInput),
            onSearchClearClick = acceptable(OnSearchClearClick),
            onFilterClick = acceptable(::OnFilterClick),
            onReviewClick = acceptable(::OnReviewClick),
            onRecentSearchesClearClick = acceptable(OnRecentSearchesClearClick),
            onAllFiltersClick = acceptable(OnAllFiltersClick),
        )
    }
}