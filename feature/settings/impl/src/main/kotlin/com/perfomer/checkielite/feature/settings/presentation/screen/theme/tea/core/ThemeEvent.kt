package com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal sealed interface ThemeEvent {

    class ThemeSetSuccessfully(val themeMode: ThemeMode) : ThemeEvent
}

internal sealed interface ThemeUiEvent : ThemeEvent {

    class OnOptionClick(val themeMode: ThemeMode) : ThemeUiEvent

    data object OnBackPress : ThemeUiEvent
}

internal sealed interface ThemeNavigationEvent : ThemeEvent