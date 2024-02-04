package com.perfomer.checkielite.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.perfomer.checkielite.navigation.voyager.impl.NavigatorHolder
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider

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

			FadeTransition(navigator) { screen ->
				screen.Content()
			}
		}
	}
}