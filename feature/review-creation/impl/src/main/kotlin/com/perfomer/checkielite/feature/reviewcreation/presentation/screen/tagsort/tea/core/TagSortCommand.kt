package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagsort.tea.core

import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagSortResult

internal sealed interface TagSortCommand

internal sealed interface TagSortNavigationCommand : TagSortCommand {

    data object Exit : TagSortNavigationCommand

    class ExitWithResult(val result: TagSortResult) : TagSortNavigationCommand
}