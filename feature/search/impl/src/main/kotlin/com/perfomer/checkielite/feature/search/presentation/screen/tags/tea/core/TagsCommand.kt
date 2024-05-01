package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core

import com.perfomer.checkielite.feature.search.presentation.navigation.TagsResult

internal sealed interface TagsCommand {

    class LoadTags(val searchQuery: String = "") : TagsCommand
}

internal sealed interface TagsNavigationCommand : TagsCommand {

    data object Exit : TagsNavigationCommand

    class ExitWithResult(val result: TagsResult) : TagsNavigationCommand
}