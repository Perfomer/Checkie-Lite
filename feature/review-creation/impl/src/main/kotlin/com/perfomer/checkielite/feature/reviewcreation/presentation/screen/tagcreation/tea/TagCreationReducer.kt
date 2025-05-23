package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.navigation.TagCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.CreateTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.DeleteTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.LoadEmojis
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.LoadTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.UpdateTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.ValidateTagName
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.CollapseTagValueField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.FocusTagValueField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowErrorToast
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.ShowTagDeleteConfirmationDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEffect.VibrateError
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEmojiCategory
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.EmojisLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagDeletion
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagNameValidated
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagSaving
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteConfirmClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDeleteTagClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnDoneClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnEmojiSelect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnSelectedEmojiClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationUiEvent.OnTagValueInput
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagInvalidReason
import kotlinx.collections.immutable.toPersistentList

internal class TagCreationReducer : DslReducer<TagCreationCommand, TagCreationEffect, TagCreationEvent, TagCreationState>() {

    override fun reduce(event: TagCreationEvent) = when (event) {
        is Initialize -> reduceInitialize()

        is TagCreationUiEvent -> reduceUi(event)

        is EmojisLoading -> reduceEmojisLoading(event)
        is TagLoading -> reduceTagLoading(event)
        is TagSaving -> reduceTagSaving(event)
        is TagDeletion -> reduceTagDeletion(event)
        is TagNameValidated -> reduceTagNameValidated(event)
    }

    private fun reduceInitialize() {
        when (val mode = state.mode) {
            is TagCreationMode.Modification -> commands(LoadTag(mode.tagId))
            is TagCreationMode.Creation -> {
                state { copy(tagValue = mode.initialTagValue) }
                if (mode.initialTagValue.isBlank()) effects(FocusTagValueField)
            }
        }

        commands(LoadEmojis)
    }

    private fun reduceUi(event: TagCreationUiEvent) = when (event) {
        is OnBackPress -> {
            if (state.isBusy) Unit // do nothing
            else commands(Exit)
        }
        is OnEmojiSelect -> state { copy(selectedEmoji = event.emoji, hasEmoji = true) }
        is OnSelectedEmojiClick -> state { copy(hasEmoji = !hasEmoji) }
        is OnTagValueInput -> {
            state { copy(tagValue = event.text) }

            val tagId = (state.mode as? TagCreationMode.Modification)?.tagId
            commands(ValidateTagName(id = tagId, name = state.tagValue.trim()))
        }
        is OnDeleteTagClick -> effects(ShowTagDeleteConfirmationDialog)
        is OnDeleteConfirmClick -> {
            val modificationMode = state.mode as TagCreationMode.Modification
            commands(DeleteTag(id = modificationMode.tagId))
        }
        is OnDoneClick -> reduceOnDoneClick()
    }

    private fun reduceEmojisLoading(event: EmojisLoading) = when (event) {
        is EmojisLoading.Started -> Unit
        is EmojisLoading.Succeed -> state {
            copy(
                emojis = event.emojis
                    .map { category ->
                        TagCreationEmojiCategory(
                            type = category.type,
                            emojis = category.groups
                                .flatMap { it.emojis }
                                .map { it.char },
                        )
                    }
                    .toPersistentList()
            )
        }
        is EmojisLoading.Failed -> commands(Exit)
    }

    private fun reduceTagLoading(event: TagLoading) = when (event) {
        is TagLoading.Started -> Unit
        is TagLoading.Succeed -> state {
            copy(
                tagValue = event.tag.value,
                selectedEmoji = event.tag.emoji,
                hasEmoji = event.tag.emoji != null,
            )
        }
        is TagLoading.Failed -> commands(Exit)
    }

    private fun reduceTagSaving(event: TagSaving) = when (event) {
        is TagSaving.Started -> state { copy(isSaving = true) }
        is TagSaving.Succeed -> {
            val result = when (state.mode) {
                is TagCreationMode.Creation -> TagCreationResult.Created(event.tag.id)
                is TagCreationMode.Modification -> TagCreationResult.Modified(event.tag.id)
            }

            commands(ExitWithResult(result))
        }
        is TagSaving.Failed -> {
            state { copy(isSaving = false) }
            effects(ShowErrorToast.SavingFailed)
        }
    }

    private fun reduceTagDeletion(event: TagDeletion) = when (event) {
        is TagDeletion.Started -> state { copy(isDeleting = true) }
        is TagDeletion.Succeed -> {
            val modificationMode = state.mode as TagCreationMode.Modification
            commands(ExitWithResult(TagCreationResult.Deleted(modificationMode.tagId)))
        }
        is TagDeletion.Failed -> {
            state { copy(isDeleting = false) }
            effects(ShowErrorToast.DeletionFailed)
        }
    }

    private fun reduceOnDoneClick() {
        if (state.tagValue.isBlank()) {
            state { copy(tagInvalidReason = TagInvalidReason.NAME_IS_EMPTY) }
        }

        if (state.tagInvalidReason != null) {
            effects(CollapseTagValueField, VibrateError, FocusTagValueField)
            return
        }

        when (val mode = state.mode) {
            is TagCreationMode.Creation -> {
                commands(
                    CreateTag(
                        value = state.tagValue.trim(),
                        emoji = state.selectedEmoji.takeIf { state.hasEmoji }),
                )
            }

            is TagCreationMode.Modification -> {
                commands(
                    UpdateTag(
                        id = mode.tagId,
                        value = state.tagValue.trim(),
                        emoji = state.selectedEmoji.takeIf { state.hasEmoji }),
                )
            }
        }
    }

    private fun reduceTagNameValidated(event: TagNameValidated) {
        when (event) {
            is TagNameValidated.Invalid -> {
                state { copy(tagInvalidReason = event.reason) }
            }
            is TagNameValidated.Valid -> {
                state { copy(tagInvalidReason = null) }
            }
        }
    }
}