package com.perfomer.checkielite.navigation.voyager

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.perfomer.checkielite.core.navigation.api.CheckieScreen

abstract class BaseScreen : Screen, CheckieScreen {

	@Composable
	final override fun Content() {
		Screen()
	}

	@Composable
	abstract fun Screen()
}