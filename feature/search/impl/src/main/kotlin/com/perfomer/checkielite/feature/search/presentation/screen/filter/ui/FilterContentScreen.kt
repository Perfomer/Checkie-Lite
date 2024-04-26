package com.perfomer.checkielite.feature.search.presentation.screen.filter.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.search.presentation.navigation.FilterParams
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.FilterStore
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core.FilterUiEvent.OnDoneClick
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class FilterContentScreen(
    private val params: FilterParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<FilterStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        FilterScreen(
            state = state,
            onDoneClick = acceptable(OnDoneClick),
        )
    }
}