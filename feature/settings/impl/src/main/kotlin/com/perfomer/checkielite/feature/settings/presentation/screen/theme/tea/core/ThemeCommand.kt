package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal sealed interface ThemeCommand {

    class SetTheme(val themeMode: ThemeMode) : ThemeCommand
}

internal sealed interface ThemeNavigationCommand : ThemeCommand {

    data object Exit : ThemeNavigationCommand
}