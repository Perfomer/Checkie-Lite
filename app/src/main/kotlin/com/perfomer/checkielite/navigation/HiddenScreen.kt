package com.perfomer.checkielite.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.perfomer.checkielite.core.navigation.api.NoScreen

internal object HiddenScreen : Screen, NoScreen {

    @Composable
    override fun Content() {
        Spacer(Modifier.fillMaxSize())
    }

    private fun readResolve(): Any = HiddenScreen
}