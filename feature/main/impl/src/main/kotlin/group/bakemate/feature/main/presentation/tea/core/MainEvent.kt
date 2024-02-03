package group.bakemate.feature.main.presentation.tea.core

import group.bakemate.feature.main.presentation.entity.ContentType

internal sealed interface MainEvent

internal sealed interface MainUiEvent : MainEvent {

    object Initialize : MainUiEvent

    class OnClick(val type: ContentType) : MainUiEvent
}