package com.perfomer.checkielite.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.navigation.voyager.impl.NavigatorHolder

class SingleStackScreenContent(
    private val navigatorHolder: NavigatorHolder,
    private val mainScreenProvider: MainScreenProvider,
) {

    @Composable
    fun Content() {
        Navigator(
            screen = mainScreenProvider() as Screen,
            onBackPressed = { false }
        ) { navigator ->
            navigatorHolder.navigator = navigator

            SlideTransition(navigator = navigator) { screen ->
                screen.Content()
            }
        }
    }
}