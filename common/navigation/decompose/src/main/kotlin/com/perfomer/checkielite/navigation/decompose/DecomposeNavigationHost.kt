package com.perfomer.checkielite.navigation.decompose

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
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
import com.perfomer.checkielite.core.navigation.transition.LocalSharedTransitionScope
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationTransitionDurationMillis

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
        MainRoot()

        BottomSheetRoot(
            controller = bottomSheetController,
            content = bottomSheetContent,
        )

        OverlayRoot(
            content = overlayContent,
        )
    }

    @Composable
    private fun MainRoot() {
        val mainNavigationStack by root.mainNavigationStack.subscribeAsState()
        val animationScope = rememberCoroutineScope()
        val sharedBackHandler = remember(root, animationScope) {
            SharedTransitionBackHandler(delegate = root.backHandler, scope = animationScope)
        }

        val animation = remember(root, sharedBackHandler) {
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
                            // Decompose seeks AnimatedVisibility only without a custom animatable.
                            // This lets the same shared image follow and reverse the back gesture.
                            if (isSharedTransition) null else androidPredictiveBackAnimatableV2(backEvent)
                        },
                    )
                },
                selector = { child, otherChild, direction, _ ->
                    if (child.configuration.hasSharedTransitionWith(otherChild.configuration, direction)) {
                        sharedAnimator
                    } else {
                        defaultAnimator
                    }
                },
            )
        }

        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                ChildStack(
                    stack = mainNavigationStack,
                    animation = animation,
                ) { child ->
                    CompositionLocalProvider(LocalNavigationAnimatedVisibilityScope provides this) {
                        child.instance.Screen()
                    }
                }
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

    @Composable
    private fun OverlayRoot(
        content: @Composable (@Composable () -> Unit) -> Unit,
    ) {
        val overlaySlot by root.overlaySlot.subscribeAsState()

        var localScreen: Screen? by remember { mutableStateOf(null) }
        val visibleState = remember { MutableTransitionState(false) }

        LaunchedEffect(overlaySlot.child?.configuration) {
            val child = overlaySlot.child

            if (child != null) {
                localScreen = child.instance
                visibleState.targetState = true
            } else {
                visibleState.targetState = false
            }
        }

        LaunchedEffect(visibleState.currentState) {
            if (!visibleState.currentState) {
                localScreen = null
            }
        }

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            content {
                localScreen?.Screen()
            }
        }
    }
}
