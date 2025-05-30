package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

import com.perfomer.checkielite.feature.search.presentation.navigation.SortResult

internal sealed interface SortCommand

internal sealed interface SortNavigationCommand : SortCommand {

    data object Exit : SortNavigationCommand

    class ExitWithResult(val result: SortResult) : SortNavigationCommand
}