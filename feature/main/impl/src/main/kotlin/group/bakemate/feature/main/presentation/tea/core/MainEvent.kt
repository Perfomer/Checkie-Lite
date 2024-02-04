package group.bakemate.feature.main.presentation.tea.core

internal sealed interface MainEvent

internal sealed interface MainUiEvent : MainEvent {

    data object Initialize : MainUiEvent

    class OnReviewClick(id: String) : MainUiEvent

    class OnSearchQueryInput(query: String) : MainUiEvent

    data object OnFabClick : MainUiEvent
}