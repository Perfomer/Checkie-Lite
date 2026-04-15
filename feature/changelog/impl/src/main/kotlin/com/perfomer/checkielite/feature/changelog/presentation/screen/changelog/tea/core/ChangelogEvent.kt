package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core

import com.perfomer.checkielite.common.pure.state.Lce

internal sealed interface ChangelogEvent {

    data object Initialize : ChangelogEvent

    class ChangelogStatusUpdated(val status: Lce<String>) : ChangelogEvent
}

internal sealed interface ChangelogUiEvent : ChangelogEvent {

    data object OnBackPress : ChangelogUiEvent
    data object OnRetryClick : ChangelogUiEvent
}

internal sealed interface ChangelogNavigationEvent : ChangelogEvent
