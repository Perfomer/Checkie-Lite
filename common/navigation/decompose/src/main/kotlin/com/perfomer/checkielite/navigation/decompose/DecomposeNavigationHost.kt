package com.perfomer.checkielite.navigation.decompose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
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
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationHost
import com.perfomer.checkielite.core.navigation.Screen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalDecomposeApi::class)
internal class DecomposeNavigationHost : NavigationHost, KoinComponent {

    @Stable
    private lateinit var root: DecomposeRootComponent

    private val navigation: DecomposeNavigatorHolder by inject()

    context(ComponentActivity)
    override fun initialize(startDestination: Destination) {
        root = retainedComponent { componentContext ->
            DecomposeRootComponent(
                componentContext = componentContext,
                startDestination = startDestination,
            )
        }
    }

    @Composable
    override fun Root() {
        MainRoot()
        BottomSheetRoot()
        OverlayRoot()
    }

    @Composable
    private fun MainRoot() {
        val mainNavigationStack by root.navigationStack.subscribeAsState()

        Children(
            stack = mainNavigationStack,
            animation = predictiveBackAnimation(
                backHandler = root.backHandler,
                fallbackAnimation = stackAnimation(slide()),
                selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
                onBack = { navigation.mainNavigator.pop() },
            ),
            content = { child -> child.instance.Screen() },
        )
    }

    @Composable
    private fun BottomSheetRoot() {
        val bottomSheetSlot by root.bottomSheetSlot.subscribeAsState()

        // TODO: Hide `Composables` library details from here.
        //   It should be more abstract.
        val sheetState = rememberModalBottomSheetState(
            initialDetent = SheetDetent.Hidden,
            positionalThreshold = { totalDistance -> (totalDistance / 4).coerceIn(128.dp, 384.dp) },
            velocityThreshold = { 24_576.dp },
        )

        var localScreen: Screen? by remember { mutableStateOf(null) }

        LaunchedEffect(bottomSheetSlot.child != null) {
            val child = bottomSheetSlot.child

            if (child != null) {
                localScreen = child.instance
                sheetState.animateTo(SheetDetent.FullyExpanded)
            } else {
                if (sheetState.targetDetent != SheetDetent.Hidden) {
                    sheetState.animateTo(SheetDetent.Hidden)
                }

                localScreen = null
            }
        }

        BottomSheetHost(
            sheetState = sheetState,
            onBackPress = { navigation.bottomSheetNavigator.dismiss() },
            content = { localScreen?.Screen() },
        )
    }

    @Composable
    private fun OverlayRoot() {
        val overlaySlot by root.overlaySlot.subscribeAsState()

        // TODO: Transition Animation
        overlaySlot.child?.instance?.Screen()
    }
}