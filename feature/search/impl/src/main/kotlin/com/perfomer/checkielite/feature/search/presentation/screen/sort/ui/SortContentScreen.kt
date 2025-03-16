package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortStore
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnSortingOptionClick

internal class SortContentScreen(
    private val store: SortStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        SortScreen(
            state = state,
            onOptionClick = acceptable(::OnSortingOptionClick),
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}