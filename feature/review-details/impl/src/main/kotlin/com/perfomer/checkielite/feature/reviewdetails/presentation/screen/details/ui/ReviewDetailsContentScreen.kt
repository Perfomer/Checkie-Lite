package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.common.ui.util.store
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsParams
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.ReviewDetailsStore
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnBackPress
import com.perfomer.checkielite.navigation.voyager.BaseScreen

internal class ReviewDetailsContentScreen(
    private val params: ReviewDetailsParams,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<ReviewDetailsStore>(params)) { state ->
        BackHandler { accept(OnBackPress) }

        ReviewDetailsScreen(
            state = state,
            onNavigationIconClick = acceptable(OnBackPress),
        )
    }
}