package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

internal sealed interface SearchCommand

internal sealed interface SearchNavigationCommand : SearchCommand {

    data object Exit : SearchNavigationCommand
}