package com.perfomer.checkielite.feature.search.presentation.screen.sort.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.BackHandlerWithLifecycle
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.search.presentation.navigation.SortParams
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.SortStore
import com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core.SortUiEvent.OnBackPress
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class SortContentScreen(
    private val params: SortParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<SortStore>(params)) { state ->
        BackHandlerWithLifecycle { accept(OnBackPress) }

        SortScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
        )
    }
}