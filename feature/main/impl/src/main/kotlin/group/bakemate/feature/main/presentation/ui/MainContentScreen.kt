package group.bakemate.feature.main.presentation.ui

import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.acceptable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import group.bakemate.feature.main.presentation.tea.MainStore
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.OnFabClick
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.OnReviewClick
import group.bakemate.feature.main.presentation.tea.core.MainUiEvent.OnSearchQueryInput

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