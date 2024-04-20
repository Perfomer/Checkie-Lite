package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.data.datasource.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.data.datasource.sort.SortingOrder

internal sealed interface SearchCommand

internal sealed interface SearchNavigationCommand : SearchCommand {

    data object Exit : SearchNavigationCommand

    class OpenSort(
        val order: SortingOrder,
        val strategy: ReviewsSortingStrategy,
    ) : SearchNavigationCommand
}