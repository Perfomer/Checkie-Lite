package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

internal sealed interface TagCreationCommand

internal sealed interface TagCreationNavigationCommand : TagCreationCommand {

    data object Exit : TagCreationNavigationCommand
}