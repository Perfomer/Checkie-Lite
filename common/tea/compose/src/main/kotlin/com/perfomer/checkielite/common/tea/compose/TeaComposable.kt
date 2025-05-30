package com.perfomer.checkielite.common.tea.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import com.perfomer.checkielite.common.tea.Store
import kotlinx.coroutines.Dispatchers

@Composable
fun <UiState : Any, UiEvent : Any, Effect : Any> TeaComposable(
    store: Store<Effect, UiEvent, UiState>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    content: @Composable TeaScope<UiEvent, Effect>.(state: UiState) -> Unit,
) {
	val scope = rememberTeaScope(lifecycleState = lifecycleState, store = store)
	val state by store.state.collectAsStateOnLifecycle(Dispatchers.Main.immediate, lifecycleState)

	content(scope, state)
}