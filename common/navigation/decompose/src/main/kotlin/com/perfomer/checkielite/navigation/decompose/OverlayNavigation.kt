package com.perfomer.checkielite.navigation.decompose

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.EnterExitState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.Modifier
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.rememberNavigationEventDispatcherOwner
import com.arkivanov.decompose.Child
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageRegistry
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationTween
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

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
    // Compose both endpoints before changing their visibility. Starting animateTo in the same
    // effect that inserts the target loses its initial bounds and produces a late match.
    val displayed = overlay ?: retained
    val placement = remember(displayed, overlay != null) { CompletableDeferred<Unit>() }
    val state = remember(displayed) { SeekableTransitionState(false) }
    val transition = rememberTransition(state, label = "Overlay navigation")
    // A closing pair must capture the visible endpoint again: a gesture may have moved it.
    val registry = remember(displayed, overlay != null) { SharedNavigationImageRegistry() }
    val currentOverlay by rememberUpdatedState(overlay)
    val backGestureMutex = remember { Mutex() }

    LaunchedEffect(overlay) {
        if (overlay != null) {
            origin = source
            retained = overlay
            placement.await()
            // Matching is observed after layout; let that snapshot reach Compose before seeking.
            withFrameNanos { }
            state.animateTo(true, animationSpec = sharedNavigationTween())
        } else {
            if (retained != null) {
                placement.await()
                withFrameNanos { }
            }
            state.animateTo(false, animationSpec = sharedNavigationTween())
            retained = null
        }
    }

    // The standalone image protocol uses the compatibility group and its own pair registry.
    val shared = displayed?.let {
        SharedContentGroup.Default in NavigationRegistry.sharedTransitionGroups(origin, it.configuration)
    } == true && source === origin
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

    val mainBackOwner = rememberNavigationEventDispatcherOwner(enabled = displayed == null)
    Box {
        CompositionLocalProvider(
            LocalNavigationEventDispatcherOwner provides mainBackOwner,
            LocalSharedNavigationImageScope provides sourceScope.takeIf { shared },
        ) {
            mainContent()
        }
    }
    displayed?.let { child ->
        CompositionLocalProvider(LocalSharedNavigationImageScope provides targetScope.takeIf { shared }) {
            Box(
                modifier = Modifier
                    .onGloballyPositioned { placement.complete(Unit) }
                    .graphicsLayer { alpha = overlayAlpha }
            ) {
                overlayContent { child.instance.Screen() }
            }
        }
    }

    // The underlying branch is disabled while the overlay is displayed, including its exit.
    // Registration order alone cannot protect us from handlers in newly composed screens.
    PredictiveBackHandler(enabled = displayed != null) { events ->
        backGestureMutex.withLock {
            val entry = currentOverlay
            try {
                events.collect {
                    if (entry != null) state.seekTo(it.progress, targetState = false)
                }
                if (entry != null && currentOverlay === entry) {
                    state.animateTo(false, animationSpec = sharedNavigationTween())
                    if (currentOverlay === entry) onBack()
                }
            } catch (_: CancellationException) {
                if (entry != null && currentOverlay === entry) {
                    withContext(NonCancellable) {
                        state.animateTo(true, animationSpec = sharedNavigationTween())
                    }
                }
            }
        }
    }
}

private class ImageVisibilityScope(
    override val transition: Transition<EnterExitState>,
) : AnimatedVisibilityScope
