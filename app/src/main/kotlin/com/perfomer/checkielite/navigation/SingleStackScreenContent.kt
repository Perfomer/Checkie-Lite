package com.perfomer.checkielite.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import group.bakemate.core.navigation.voyager.impl.NavigatorHolder
import group.bakemate.feature.main.navigation.MainScreenProvider
import group.bakemate.feature.splash.navigation.SplashScreenProvider

class SingleStackScreenContent(
    private val navigatorHolder: NavigatorHolder,
    private val splashScreenProvider: SplashScreenProvider,
    private val mainScreenProvider: MainScreenProvider,
) {

	@Composable
	fun Content() {
		Navigator(
			screen = mainScreenProvider() as Screen,
			onBackPressed = { false }
		) { navigator ->
			navigatorHolder.navigator = navigator

			FadeTransition(navigator) { screen ->
				screen.Content()
			}
		}
	}
}