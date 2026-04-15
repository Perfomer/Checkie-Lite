package com.perfomer.checkielite.feature.changelog.presentation.screen.changelog.tea.core

internal sealed interface ChangelogCommand {

    data object LoadChangelog : ChangelogCommand

    class MarkVersionAsSeen(val versionCode: Int) : ChangelogCommand
}

internal sealed interface ChangelogNavigationCommand : ChangelogCommand {

    data object Exit : ChangelogNavigationCommand
}
