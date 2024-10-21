package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.feature.reviewcreation.domain.entity.CheckieEmojiCategory

internal sealed interface TagCreationEvent {

    data object Initialize : TagCreationEvent

    sealed interface EmojisLoading : TagCreationEvent {
        data object Started : EmojisLoading
        class Succeed(val emojis: List<CheckieEmojiCategory>) : EmojisLoading
        class Failed(val throwable: Throwable) : EmojisLoading
    }

    sealed interface TagLoading : TagCreationEvent {
        data object Started : TagLoading
        class Succeed(val tag: CheckieTag) : TagLoading
        class Failed(val throwable: Throwable) : TagLoading
    }

    sealed interface TagSaving : TagCreationEvent {
        data object Started : TagSaving
        class Succeed(val tag: CheckieTag) : TagSaving
        class Failed(val throwable: Throwable) : TagSaving
    }

    sealed interface TagDeletion : TagCreationEvent {
        data object Started : TagDeletion
        data object Succeed : TagDeletion
        class Failed(val throwable: Throwable) : TagDeletion
    }

    sealed interface TagNameValidated : TagCreationEvent {
        data object Valid : TagNameValidated
        class Invalid(val reason: TagInvalidReason) : TagNameValidated
    }
}

internal sealed interface TagCreationUiEvent : TagCreationEvent {

    data object OnBackPress : TagCreationUiEvent

    data object OnSelectedEmojiClick : TagCreationUiEvent

    class OnTagValueInput(val text: String) : TagCreationUiEvent

    class OnEmojiSelect(val emoji: String) : TagCreationUiEvent

    data object OnDoneClick : TagCreationUiEvent

    data object OnDeleteTagClick : TagCreationUiEvent

    data object OnDeleteConfirmClick : TagCreationUiEvent
}

internal sealed interface TagCreationNavigationEvent : TagCreationEvent