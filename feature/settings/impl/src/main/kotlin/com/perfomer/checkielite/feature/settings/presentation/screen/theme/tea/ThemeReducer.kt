package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeCommand.SetTheme
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEffect
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeEvent.ThemeSetSucceed
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeNavigationCommand.Exit
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeState
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeUiEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeUiEvent.OnBackPress
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core.ThemeUiEvent.OnOptionClick

internal class ThemeReducer : DslReducer<ThemeCommand, ThemeEffect, ThemeEvent, ThemeState>() {

    override fun reduce(event: ThemeEvent) = when (event) {
        is ThemeSetSucceed -> state { copy(currentTheme = event.themeMode) }
        is ThemeUiEvent -> reduceUi(event)
    }

    private fun reduceUi(event: ThemeUiEvent) = when (event) {
        is OnOptionClick -> commands(SetTheme(themeMode = event.themeMode))
        is OnBackPress -> commands(Exit)
    }
}