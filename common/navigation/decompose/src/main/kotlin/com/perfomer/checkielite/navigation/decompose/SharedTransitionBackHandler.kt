package com.perfomer.checkielite.navigation.decompose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.withFrameNanos
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.backhandler.BackHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/** Smooths Decompose's immediate predictive cancellation while retaining its seekable transition. */
internal class SharedTransitionBackHandler(
    private val delegate: BackHandler,
    private val scope: CoroutineScope,
    private val animateCancellation: suspend (BackEvent, (BackEvent) -> Unit) -> Unit = ::animateBackCancellation,
    private val awaitFrame: suspend () -> Unit = { withFrameNanos {} },
) : BackHandler {

    private val callbacks = mutableMapOf<BackCallback, AnimatedCallback>()

    override fun isRegistered(callback: BackCallback): Boolean = callback in callbacks

    override fun register(callback: BackCallback) {
        check(callback !in callbacks) { "Callback is already registered" }
        val animatedCallback = AnimatedCallback(callback)
        callbacks[callback] = animatedCallback
        delegate.register(animatedCallback)
    }

    override fun unregister(callback: BackCallback) {
        val animatedCallback = checkNotNull(callbacks.remove(callback)) { "Callback is not registered" }
        // The delegate can call onBackCancelled during unregister; the closed queue ignores it.
        animatedCallback.dispose()
        delegate.unregister(animatedCallback)
    }

    private inner class AnimatedCallback(
        private val callback: BackCallback,
    ) : BackCallback(isEnabled = callback.isEnabled, priority = callback.priority) {

        private val events = Channel<GestureEvent>(capacity = Channel.UNLIMITED)
        private val enabledListener: (Boolean) -> Unit = { isEnabled = it }
        private val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
            var lastEvent: BackEvent? = null

            // Queue a new gesture until the previous rollback has restored both screen transitions.
            for (event in events) {
                when (event) {
                    is GestureEvent.Started -> {
                        lastEvent = event.value
                        callback.onBackStarted(event.value)
                    }

                    is GestureEvent.Progressed -> {
                        lastEvent = event.value
                        callback.onBackProgressed(event.value)
                    }

                    GestureEvent.Cancelled -> {
                        lastEvent?.takeIf { it.progress > 0F }?.let {
                            animateCancellation(it, callback::onBackProgressed)
                            // Let the final seek to zero reach composition before Decompose snaps.
                            awaitFrame()
                        }
                        callback.onBackCancelled()
                        lastEvent = null
                        // Decompose resets its animation handler in a coroutine.
                        awaitFrame()
                    }

                    GestureEvent.Completed -> {
                        lastEvent = null
                        callback.onBack()
                    }
                }
            }
        }

        init {
            callback.addEnabledChangedListener(enabledListener)
        }

        override fun onBackStarted(backEvent: BackEvent) {
            events.trySend(GestureEvent.Started(backEvent))
        }

        override fun onBackProgressed(backEvent: BackEvent) {
            events.trySend(GestureEvent.Progressed(backEvent))
        }

        override fun onBackCancelled() {
            events.trySend(GestureEvent.Cancelled)
        }

        override fun onBack() {
            events.trySend(GestureEvent.Completed)
        }

        fun dispose() {
            callback.removeEnabledChangedListener(enabledListener)
            events.close()
            job.cancel()
        }
    }

    private sealed interface GestureEvent {
        data class Started(val value: BackEvent) : GestureEvent
        data class Progressed(val value: BackEvent) : GestureEvent
        data object Cancelled : GestureEvent
        data object Completed : GestureEvent
    }
}

private suspend fun animateBackCancellation(
    event: BackEvent,
    onProgress: (BackEvent) -> Unit,
) {
    animate(
        initialValue = event.progress,
        targetValue = 0F,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
    ) { progress, _ ->
        onProgress(event.copy(progress = progress.coerceIn(0F, 1F)))
    }
}
