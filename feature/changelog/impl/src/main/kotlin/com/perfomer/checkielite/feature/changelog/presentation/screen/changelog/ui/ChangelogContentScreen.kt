package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.ChangelogStore
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent.OnBackPress
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent.OnRetryClick

internal class ChangelogContentScreen(private val store: ChangelogStore) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        ChangelogScreen(
            state = state,
            onBackPress = acceptable(OnBackPress),
            onRetryClick = acceptable(OnRetryClick),
        )
    }
}
