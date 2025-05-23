package com.perfomer.checkielite.common.ui.util

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.perfomer.checkielite.common.tea.impl.ScreenModelStore
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ScreenModelStore<*, *, *, *, *, *>> Screen.store(params: Any? = null): T {
	return getScreenModel { parametersOf(params) }
}