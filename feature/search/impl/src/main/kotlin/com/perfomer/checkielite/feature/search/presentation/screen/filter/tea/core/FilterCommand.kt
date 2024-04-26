package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core

import com.perfomer.checkielite.feature.search.presentation.navigation.FilterResult

internal sealed interface FilterCommand

internal sealed interface FilterNavigationCommand : FilterCommand {

    data object Exit : FilterNavigationCommand

    class ExitWithResult(val result: FilterResult) : FilterNavigationCommand
}