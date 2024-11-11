package com.perfomer.checkielite.navigation.decompose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.NavigationHost

internal class DecomposeNavigationHost : NavigationHost {

    @Stable
    private lateinit var root: DecomposeRootComponent

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
    @OptIn(ExperimentalDecomposeApi::class)
    override fun Root() {
        val childStack by root.childStack.subscribeAsState()

        Children(
            stack = childStack,
            animation = predictiveBackAnimation(
                backHandler = root.backHandler,
                fallbackAnimation = stackAnimation(slide()),
                selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
                onBack = root::onBackClicked,
            ),
            content = { child -> child.instance.Screen() },
        )
    }
}