package com.perfomer.checkielite.navigation.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.perfomer.checkielite.core.navigation.api.CheckieScreen

abstract class BaseScreen : Screen, CheckieScreen {

	override val key: ScreenKey = uniqueScreenKey

	@Composable
	final override fun Content() {
		Screen()
	}

	@Composable
	abstract fun Screen()
}