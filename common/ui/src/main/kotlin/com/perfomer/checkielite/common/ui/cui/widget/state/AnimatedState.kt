package com.perfomer.checkielite.common.ui.cui.widget.state

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.perfomer.checkielite.common.pure.util.cleanLoading
import com.perfomer.checkielite.common.ui.util.crossfade
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun <S : ViewState> AnimatedState(
    state: S,
    label: String = "AnimatedState",
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    val stateFlow = remember { MutableStateFlow(state) }

    LaunchedEffect(state) {
        stateFlow.emit(state)
    }

    val animatedStateFlow = remember(stateFlow) {
        stateFlow.cleanLoading(
            defineLoading = { it is ViewState.Loading },
        )
    }
    val animatedState by animatedStateFlow.collectAsState(state)

    AnimatedContent(
        targetState = animatedState,
        contentKey = { it::class },
        label = label,
        transitionSpec = { crossfade(tween(300)) },
        content = content,
    )
}
