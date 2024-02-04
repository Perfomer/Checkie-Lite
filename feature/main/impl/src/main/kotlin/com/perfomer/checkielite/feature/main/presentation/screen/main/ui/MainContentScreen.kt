package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSearchQueryClearClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSearchQueryInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class MainContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<MainStore>()) { state ->
        MainScreen(
            state = state,
            onReviewClick = acceptable(::OnReviewClick),
            onSearchQueryInput = acceptable(::OnSearchQueryInput),
            onSearchQueryClearClick = acceptable(OnSearchQueryClearClick),
            onFabClick = acceptable(OnFabClick),
        )
    }
}