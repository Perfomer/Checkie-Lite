package com.perfomer.checkielite.feature.main.presentation.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.ui.tea.TeaComposable
import com.perfomer.checkielite.common.ui.tea.acceptable
import com.perfomer.checkielite.common.ui.tea.store
import com.perfomer.checkielite.feature.main.presentation.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnSearchQueryInput
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class MainContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<MainStore>()) { state ->
        MainScreen(
            state = state,
            onReviewClick = acceptable(::OnReviewClick),
            onSearchQueryInput = acceptable(::OnSearchQueryInput),
            onFabClick = acceptable(OnFabClick),
        )
    }
}