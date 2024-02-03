package group.bakemate.common.tea.util

import androidx.lifecycle.LifecycleOwner
import group.bakemate.tea.tea.Store
import group.bakemate.tea.tea.component.Renderer
import group.bakemate.tea.tea.util.launchRepeatOnResumed
import group.bakemate.tea.tea.util.launchRepeatOnStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Binds view with store
 *
 * @param lifecycleOwner - lifecycle owner with lifecycle
 * @param stateRenderer - renderState function with [UiState]
 * @param effectRenderer - renderEffect function with [Effect]
 */
fun <Effect : Any, UiState : Any> Store<Effect, *, UiState>.bind(
    lifecycleOwner: LifecycleOwner,
    stateRenderer: Renderer<UiState>? = null,
    effectRenderer: Renderer<Effect>? = null,
) {
	if (stateRenderer != null) {
		lifecycleOwner.launchRepeatOnStarted {
			state.onEach(stateRenderer::render).launchIn(scope = this)
		}
	}

	if (effectRenderer != null) {
		lifecycleOwner.launchRepeatOnResumed {
			effects.onEach(effectRenderer::render).launchIn(scope = this)
		}
	}
}