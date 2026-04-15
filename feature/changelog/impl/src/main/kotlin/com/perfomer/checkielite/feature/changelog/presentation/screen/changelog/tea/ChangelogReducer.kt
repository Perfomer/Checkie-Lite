package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea

import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand.LoadChangelog
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand.MarkVersionAsSeen
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEffect
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent.ChangelogStatusUpdated
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogEvent.Initialize
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogNavigationCommand.Exit
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogNavigationEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogState
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent.OnBackPress
import com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogUiEvent.OnRetryClick

internal class ChangelogReducer : DslReducer<com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core.ChangelogCommand, ChangelogEffect, ChangelogEvent, ChangelogState>() {

    override fun reduce(event: ChangelogEvent) = when (event) {
        is ChangelogUiEvent -> reduceUi(event)
        is ChangelogNavigationEvent -> reduceNavigation(event)
        is Initialize -> reduceInitialize()
        is ChangelogStatusUpdated -> onChangelogStatusUpdated(event)
    }

    private fun reduceInitialize() {
        commands(LoadChangelog)
    }

    private fun reduceUi(event: ChangelogUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnRetryClick -> commands(LoadChangelog)
    }

    private fun reduceNavigation(event: ChangelogNavigationEvent) = when (event) {
        else -> Unit
    }

    private fun onChangelogStatusUpdated(event: ChangelogStatusUpdated) {
        state { copy(changelog = event.status) }
        commands(
            MarkVersionAsSeen(AppInfo.versionCode).takeIf { event.status is Lce.Content },
        )
    }
}
