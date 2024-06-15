package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationResult

internal sealed interface TagCreationCommand {

    data object LoadEmojis : TagCreationCommand

    class LoadTag(val id: String) : TagCreationCommand

    class ValidateTagName(val id: String?, val name: String) :TagCreationCommand

    class CreateTag(
        val value: String,
        val emoji: String?,
    ) : TagCreationCommand

    class UpdateTag(
        val id: String,
        val value: String,
        val emoji: String?,
    ) : TagCreationCommand

    class DeleteTag(val id: String) : TagCreationCommand
}

internal sealed interface TagCreationNavigationCommand : TagCreationCommand {

    data object Exit : TagCreationNavigationCommand

    class ExitWithResult(val result: TagCreationResult) : TagCreationNavigationCommand
}