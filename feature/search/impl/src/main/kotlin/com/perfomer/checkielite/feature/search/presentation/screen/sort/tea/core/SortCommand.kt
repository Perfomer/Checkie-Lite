package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

internal sealed interface SortCommand

internal sealed interface SortNavigationCommand : SortCommand {

    data object Exit : SortNavigationCommand
}