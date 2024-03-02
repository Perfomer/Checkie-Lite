package com.perfomer.checkielite.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.perfomer.checkielite.core.navigation.api.NoScreen

internal object HiddenScreen : Screen, NoScreen {

    @Composable
    override fun Content() {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }

    private fun readResolve(): Any = HiddenScreen
}