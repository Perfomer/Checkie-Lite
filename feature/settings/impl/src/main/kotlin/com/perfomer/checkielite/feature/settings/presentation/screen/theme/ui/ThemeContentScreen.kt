package com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui

import androidx.compose.runtime.Composable
import com.perfomer.checkielite.common.tea.compose.TeaComposable
import com.perfomer.checkielite.common.tea.compose.acceptable
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.ThemeStore
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeUiEvent.OnOptionClick

internal class ThemeContentScreen(
    private val store: ThemeStore,
) : Screen {

    @Composable
    override fun Screen() = TeaComposable(store) { state ->
        ThemeScreen(
            state = state,
            onOptionClick = acceptable(::OnOptionClick),
        )
    }
}