package com.perfomer.checkielite.navigation.decompose

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.Child
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageRegistry
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationTween
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/** Retains both endpoints until the overlay has finished leaving, independently of slot lifetime. */
@Composable
@OptIn(ExperimentalTransitionApi::class)
internal fun OverlayNavigation(
    source: Destination,
    overlay: Child.Created<Destination, Screen>?,
    onBack: () -> Unit,
    mainContent: @Composable () -> Unit,
    overlayContent: @Composable (@Composable () -> Unit) -> Unit,
) {
    var retained by remember { mutableStateOf(overlay) }
    var origin by remember { mutableStateOf(source) }
    val state = remember { SeekableTransitionState(false) }
    val transition = rememberTransition(state, label = "Overlay navigation")
    val registry = remember(retained) { SharedNavigationImageRegistry() }
    val currentOverlay by rememberUpdatedState(overlay)
    val backGestureMutex = remember { Mutex() }

    LaunchedEffect(overlay) {
        if (overlay != null) {
            origin = source
            retained = overlay
            state.animateTo(true, animationSpec = sharedNavigationTween())
        } else {
            state.animateTo(false, animationSpec = sharedNavigationTween())
            retained = null
        }
    }

    PredictiveBackHandler(enabled = overlay != null) { events ->
        // A second gesture must not interrupt the previous gesture's rollback.
        backGestureMutex.withLock {
            val entry = currentOverlay ?: return@withLock
            try {
                events.collect { state.seekTo(it.progress, targetState = false) }
                state.animateTo(false, animationSpec = sharedNavigationTween())
                if (currentOverlay === entry) onBack()
            } catch (_: CancellationException) {
                if (currentOverlay != null && currentOverlay === entry) {
                    withContext(NonCancellable) {
                        state.animateTo(true, animationSpec = sharedNavigationTween())
                    }
                }
            }
        }
    }

    val shared = retained?.let { NavigationRegistry.hasSharedTransition(origin, it.configuration) } == true &&
        source === origin
    val sourceTransition = transition.createChildTransition(label = "Overlay source") {
        if (it) EnterExitState.PostExit else EnterExitState.Visible
    }
    val targetTransition = transition.createChildTransition(label = "Overlay target") {
        if (it) EnterExitState.Visible else EnterExitState.PreEnter
    }
    val sourceScope = remember(registry, sourceTransition) {
        SharedNavigationImageScope(registry, ImageVisibilityScope(sourceTransition), isTarget = false)
    }
    val targetScope = remember(registry, targetTransition) {
        SharedNavigationImageScope(registry, ImageVisibilityScope(targetTransition), isTarget = true)
    }
    val overlayAlpha by transition.animateFloat(transitionSpec = { sharedNavigationTween() }, label = "Overlay alpha") {
        if (it) 1F else 0F
    }

    Box {
        CompositionLocalProvider(LocalSharedNavigationImageScope provides sourceScope.takeIf { shared }) {
            mainContent()
        }
    }
    retained?.let { child ->
        CompositionLocalProvider(LocalSharedNavigationImageScope provides targetScope.takeIf { shared }) {
            Box(modifier = Modifier.graphicsLayer { alpha = overlayAlpha }) {
                overlayContent { child.instance.Screen() }
            }
        }
    }
}

private class ImageVisibilityScope(
    override val transition: Transition<EnterExitState>,
) : AnimatedVisibilityScope
