package com.perfomer.checkielite.navigation.decompose

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.Stable
import androidx.compose.runtime.withFrameNanos
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.perfomer.checkielite.core.navigation.BottomSheetController
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.core.navigation.NavigationRegistry
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.Screen
import com.perfomer.checkielite.core.navigation.transition.LocalNavigationAnimatedVisibilityScope
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationPair
import com.perfomer.checkielite.core.navigation.transition.LocalSharedTransitionScope
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationTransitionDurationMillis
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalDecomposeApi::class)
internal class DecomposeNavigationHost(
    private val router: Router,
) : NavigationHost {

    @Stable
    private var root: DecomposeRootComponent by DecomposeRootComponentHolder

    context(activity: ComponentActivity)
    override fun initialize(startDestination: Destination) {
        root = activity.retainedComponent { componentContext ->
            DecomposeRootComponent(
                componentContext = componentContext,
                startDestination = startDestination,
            )
        }
    }

    override fun back() {
        router.exit()
    }

    @Composable
    override fun NavigationRoot(
        bottomSheetController: BottomSheetController,
        bottomSheetContent: @Composable (@Composable () -> Unit) -> Unit,
        overlayContent: @Composable (@Composable () -> Unit) -> Unit,
    ) {
        val stack by root.mainNavigationStack.subscribeAsState()
        val overlay by root.overlaySlot.subscribeAsState()
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                OverlayNavigation(
                    source = stack.active.configuration,
                    overlay = overlay.child,
                    onBack = ::back,
                    mainContent = {
                        MainRoot()
                        CompositionLocalProvider(LocalSharedNavigationImageScope provides null) {
                            BottomSheetRoot(
                                controller = bottomSheetController,
                                content = bottomSheetContent,
                            )
                        }
                    },
                    overlayContent = overlayContent,
                )
            }
        }
    }

    @Composable
    private fun MainRoot() {
        val mainNavigationStack by root.mainNavigationStack.subscribeAsState()
        val animationScope = rememberCoroutineScope()
        val pairs = remember(root) { SharedNavigationPairs() }
        var renderedStack by remember(root) { mutableStateOf(mainNavigationStack) }
        val stackAnimations = remember(root) { mutableStateMapOf<String, Boolean>() }
        val currentStack by rememberUpdatedState(mainNavigationStack)
        LaunchedEffect(mainNavigationStack) {
            snapshotFlow { stackAnimations.values.any { it } }.first { !it }
            val previous = renderedStack
            val next = mainNavigationStack
            if (previous.active.key != next.active.key) {
                val isBack = next.items.size < previous.items.size &&
                    previous.backStack.any { it.key == next.active.key }
                val source = if (isBack) next.active else previous.active
                val target = if (isBack) previous.active else next.active
                val groups = NavigationRegistry.sharedTransitionGroups(source.configuration, target.configuration)
                if (groups.isNotEmpty()) {
                    pairs.prepare(source.key, target.key, groups)
                    // A changed shared key resets Compose's bounds provider. Place the resting
                    // source with the new key before Decompose changes either visibility state.
                    withFrameNanos { }
                    withFrameNanos { }
                }
            }
            renderedStack = next
        }
        val sharedBackHandler = remember(root, animationScope) {
            SharedTransitionBackHandler(
                delegate = root.backHandler,
                scope = animationScope,
                prepareTransition = {
                    val stack = currentStack
                    val source = stack.backStack.lastOrNull()
                    if (source != null) {
                        pairs.prepare(
                            source.key,
                            stack.active.key,
                            NavigationRegistry.sharedTransitionGroups(source.configuration, stack.active.configuration),
                        )
                        withFrameNanos { }
                        withFrameNanos { }
                    }
                },
            )
        }

        val animation = remember(root, sharedBackHandler, pairs) {
            val sharedAnimator = fade(
                animationSpec = tween(
                    durationMillis = SharedNavigationTransitionDurationMillis,
                    easing = FastOutSlowInEasing,
                ),
            )
            val defaultAnimator = slide()

            stackAnimation<Destination, Screen>(
                predictiveBackParams = { stack ->
                    val previousDestination = stack.backStack.lastOrNull()?.configuration
                    val isSharedTransition = previousDestination?.let {
                        NavigationRegistry.hasSharedTransition(
                            source = it,
                            target = stack.active.configuration,
                        )
                    } == true

                    PredictiveBackParams(
                        backHandler = if (isSharedTransition) sharedBackHandler else root.backHandler,
                        onBack = ::back,
                        animatable = { backEvent ->
                            if (previousDestination != null) {
                                pairs.select(
                                    source = stack.backStack.last().key,
                                    target = stack.active.key,
                                    groups = NavigationRegistry.sharedTransitionGroups(
                                        previousDestination,
                                        stack.active.configuration,
                                    ),
                                )
                            }
                            // Decompose seeks AnimatedVisibility only without a custom animatable.
                            // This lets the same shared image follow and reverse the back gesture.
                            if (isSharedTransition) null else androidPredictiveBackAnimatableV2(backEvent)
                        },
                    )
                },
                selector = { child, otherChild, direction, _ ->
                    val (source, target) = when (direction) {
                        Direction.ENTER_FRONT,
                        Direction.EXIT_FRONT -> otherChild to child
                        Direction.ENTER_BACK,
                        Direction.EXIT_BACK -> child to otherChild
                    }
                    pairs.select(
                        source = source.key,
                        target = target.key,
                        groups = NavigationRegistry.sharedTransitionGroups(source.configuration, target.configuration),
                    )
                    if (child.configuration.hasSharedTransitionWith(otherChild.configuration, direction)) {
                        sharedAnimator
                    } else {
                        defaultAnimator
                    }
                },
            )
        }

        val imageScope = LocalSharedNavigationImageScope.current
        ChildStack(
            stack = renderedStack,
            animation = animation,
        ) { child ->
            // Decompose can select a queued animation before the current one has finished.
            // Bind metadata to its Transition, never to the latest active stack entry.
            val transitionPair = remember(transition) { pairs.forEntry(child.key) }
            val pair = if (stackAnimationDirection == null) pairs.forEntry(child.key) else transitionPair
            val animating = stackAnimationDirection != null
            SideEffect { stackAnimations[child.key] = animating }
            DisposableEffect(child.key) {
                onDispose {
                    pairs.forget(child.key)
                    stackAnimations.remove(child.key)
                }
            }
            CompositionLocalProvider(
                LocalNavigationAnimatedVisibilityScope provides this,
                LocalSharedNavigationPair provides pair,
                LocalSharedNavigationImageScope provides imageScope.takeIf {
                    child.configuration === mainNavigationStack.active.configuration
                },
            ) {
                child.instance.Screen()
            }
        }
    }

    @Composable
    private fun BottomSheetRoot(
        controller: BottomSheetController,
        content: @Composable (@Composable () -> Unit) -> Unit,
    ) {
        val bottomSheetSlot by root.bottomSheetSlot.subscribeAsState()
        var localScreen: Screen? by remember { mutableStateOf(null) }

        LaunchedEffect(bottomSheetSlot.child?.configuration) {
            val child = bottomSheetSlot.child

            if (child != null) {
                localScreen = child.instance
                controller.show()
            } else {
                if (controller.isVisible) {
                    controller.hide()
                }
                localScreen = null
            }
        }

        content {
            localScreen?.Screen()
        }
    }
}
