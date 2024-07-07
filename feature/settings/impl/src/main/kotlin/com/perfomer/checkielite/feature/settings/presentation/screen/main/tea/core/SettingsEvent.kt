package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

internal sealed interface SettingsEvent {

    data object Initialize : SettingsEvent
}

internal sealed interface SettingsUiEvent : SettingsEvent {

    data object OnBackPress : SettingsUiEvent
}

internal sealed interface SettingsNavigationEvent : SettingsEvent