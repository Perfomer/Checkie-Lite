package com.perfomer.checkielite.common.tea.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import com.perfomer.checkielite.common.tea.Store
import kotlinx.coroutines.CoroutineScope

@Stable
interface TeaScope<in UiEvent : Any, out Effect : Any> {

	fun accept(event: UiEvent)

	@Composable
	@SuppressLint("ComposableNaming")
	fun renderEffect(renderer: suspend CoroutineScope.(Effect) -> Unit)
}

@Composable
internal fun <UiEvent : Any, Effect : Any> rememberTeaScope(
    store: Store<Effect, UiEvent, *>,
    lifecycleState: Lifecycle.State,
): TeaScope<UiEvent, Effect> {
	return remember(store, lifecycleState) {
		TeaScopeImpl(store, lifecycleState)
	}
}

private class TeaScopeImpl<in UiEvent : Any, out Effect : Any>(
    private val store: Store<Effect, UiEvent, *>,
    private val lifecycleState: Lifecycle.State,
) : TeaScope<UiEvent, Effect> {

	override fun accept(event: UiEvent) {
		store.accept(event)
	}

	@Composable
	@SuppressLint("ComposableNaming")
	override fun renderEffect(renderer: suspend CoroutineScope.(Effect) -> Unit) {
		store.effects.collectOnLifecycle(lifecycleState, renderer)
	}
}