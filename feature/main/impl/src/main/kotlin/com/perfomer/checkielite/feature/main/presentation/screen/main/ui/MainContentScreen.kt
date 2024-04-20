package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSearchClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnStart
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class MainContentScreen : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<MainStore>()) { state ->
        LifecycleEffect(
            onStarted = acceptable(OnStart),
        )

        MainScreen(
            state = state,
            onReviewClick = acceptable(::OnReviewClick),
            onSearchClick = acceptable(OnSearchClick),
            onFabClick = acceptable(OnFabClick),
        )
    }
}