package com.perfomer.checkielite.feature.main.presentation.ui

import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.acceptable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import com.perfomer.checkielite.feature.main.presentation.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.tea.core.MainUiEvent.OnSearchQueryInput

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