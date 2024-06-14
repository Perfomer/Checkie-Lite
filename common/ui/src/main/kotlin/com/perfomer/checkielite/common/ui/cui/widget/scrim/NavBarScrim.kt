package com.perfomer.checkielite.common.ui.cui.widget.scrim

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow

object NavBarScrimController {

    internal val shouldShowNavBarScrim: MutableStateFlow<Boolean> = MutableStateFlow(true)

    fun showScrim(show: Boolean) {
        this.shouldShowNavBarScrim.tryEmit(show)
    }
}

@Composable
fun NavBarScrim() {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        val shouldShowNavBarScrim by NavBarScrimController.shouldShowNavBarScrim.collectAsState()
        val height = WindowInsets.navigationBars.getBottom(LocalDensity.current).pxToDp().dp

        AnimatedVisibility(
            visible = shouldShowNavBarScrim,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            VerticalScrim(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
            )
        }
    }
}