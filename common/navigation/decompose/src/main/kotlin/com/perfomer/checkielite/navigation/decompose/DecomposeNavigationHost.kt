package com.perfomer.checkielite.navigation.decompose

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.pop
import com.perfomer.checkielite.core.navigation.BottomSheetController
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.core.navigation.Screen

@OptIn(ExperimentalDecomposeApi::class)
internal class DecomposeNavigationHost : NavigationHost {

    @Stable
    private var root: DecomposeRootComponent by DecomposeRootComponentHolder

    context(ComponentActivity)
    override fun initialize(startDestination: Destination) {
        root = retainedComponent { componentContext ->
            DecomposeRootComponent(
                componentContext = componentContext,
                startDestination = startDestination,
            )
        }
    }

    override fun hideBottomSheet() {
        root.bottomSheetNavigator.dismiss()
    }

    @Composable
    override fun Root(
        bottomSheetController: BottomSheetController,
        bottomSheetContent: @Composable (@Composable () -> Unit) -> Unit,
    ) {
        MainRoot()

        BottomSheetRoot(
            controller = bottomSheetController,
            content = bottomSheetContent,
        )

        OverlayRoot()
    }

    @Composable
    private fun MainRoot() {
        val mainNavigationStack by root.mainNavigationStack.subscribeAsState()

        Children(
            stack = mainNavigationStack,
            animation = predictiveBackAnimation(
                backHandler = root.backHandler,
                fallbackAnimation = stackAnimation(slide()),
                selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
                onBack = { root.mainNavigator.pop() },
            ),
            content = { child -> child.instance.Screen() },
        )
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

            BackHandler(
                enabled = controller.isVisible,
                onBack = { root.bottomSheetNavigator.dismiss() },
            )
        }
    }

    @Composable
    private fun OverlayRoot() {
        val overlaySlot by root.overlaySlot.subscribeAsState()

        // TODO: Transition Animation
        overlaySlot.child?.instance?.Screen()
    }
}