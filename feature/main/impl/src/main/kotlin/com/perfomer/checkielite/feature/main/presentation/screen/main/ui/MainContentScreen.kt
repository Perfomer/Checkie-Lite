package com.perfomer.checkielite.feature.main.presentation.screen.main.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.cui.widget.toast.LocalToastController
import com.perfomer.checkielite.common.ui.cui.widget.toast.rememberSuccessToast
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.main.R
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.MainStore
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect.ShowToast
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect.ShowToast.Reason
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSearchClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSettingsClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnTagClick

internal class MainContentScreen(private val store: MainStore) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        val toastController = LocalToastController.current

        val reviewCreatedToast = rememberSuccessToast(message = R.string.main_toast_reviewcreated)

        EffectHandler { effect ->
            when (effect) {
                is ShowToast -> when (effect.reason) {
                    Reason.REVIEW_CREATED -> toastController.showToast(reviewCreatedToast)
                }
            }
        }

        MainScreen(
            state = state,
            onReviewClick = acceptable(::OnReviewClick),
            onTagClick = acceptable(::OnTagClick),
            onSettingsClick = acceptable(OnSettingsClick),
            onSearchClick = acceptable(OnSearchClick),
            onFabClick = acceptable(OnFabClick),
        )
    }
}