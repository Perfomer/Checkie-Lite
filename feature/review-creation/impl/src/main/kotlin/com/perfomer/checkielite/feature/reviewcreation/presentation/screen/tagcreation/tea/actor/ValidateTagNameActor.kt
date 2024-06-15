package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor

import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.ValidateTagName
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagNameValidated.Invalid
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagNameValidated.Valid
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class ValidateTagNameActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<TagCreationCommand, TagCreationEvent> {

    override fun act(commands: Flow<TagCreationCommand>): Flow<TagCreationEvent> {
        return commands.filterIsInstance<ValidateTagName>()
            .mapLatest(::handleCommand)
    }

    private suspend fun handleCommand(command: ValidateTagName): TagCreationEvent {
        return when {
            checkIfEmpty(command.name) -> Invalid(TagInvalidReason.NAME_IS_EMPTY)
            checkIfTooLong(command.name) -> Invalid(TagInvalidReason.NAME_IS_TOO_LONG)
            checkIfAlreadyExists(command.id, command.name) -> Invalid(TagInvalidReason.NAME_CONFLICT)
            else -> Valid
        }
    }

    private suspend fun checkIfAlreadyExists(id: String?, name: String): Boolean {
        val existingTag = localDataSource.getTagByName(name)
        return existingTag != null && existingTag.id != id
    }

    private fun checkIfEmpty(name: String): Boolean {
        return name.isBlank()
    }

    private fun checkIfTooLong(name: String): Boolean {
        return name.length > MAX_TAG_LENGTH
    }

    private companion object {
        private const val MAX_TAG_LENGTH = 15
    }
}