package com.perfomer.checkielite.feature.search.presentation.screen.search.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.SearchStore
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect.ShowKeyboard
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnAllFiltersClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnFilterClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnRecentSearchesClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchFieldInput

internal class SearchContentScreen(
    private val store: SearchStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        BackHandler { accept(OnBackPress) }

        val searchFieldFocusRequester = remember { FocusRequester() }

        EffectHandler { effect ->
            when (effect) {
                ShowKeyboard -> searchFieldFocusRequester.requestFocus()
            }
        }

        SearchScreen(
            state = state,
            searchFieldFocusRequester = searchFieldFocusRequester,
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