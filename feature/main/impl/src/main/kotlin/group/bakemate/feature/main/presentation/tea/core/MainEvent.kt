package group.bakemate.feature.main.presentation.tea.core

internal sealed interface MainEvent {

    data object Initialize : MainEvent
}

internal sealed interface MainUiEvent : MainEvent {

    class OnReviewClick(val id: String) : MainUiEvent

    class OnSearchQueryInput(val query: String) : MainUiEvent

    data object OnFabClick : MainUiEvent
}