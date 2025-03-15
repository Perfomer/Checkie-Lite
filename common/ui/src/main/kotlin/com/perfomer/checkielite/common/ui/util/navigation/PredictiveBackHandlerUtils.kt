package com.perfomer.checkielite.common.ui.util.navigation

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun registerPredictiveBack(
    enabled: Boolean = true,
    onBack: () -> Unit,
): FloatState {
    val backProgress = remember { mutableFloatStateOf(0F) }

    PredictiveBackHandler(enabled = enabled) { progress ->
        try {
            progress.collect { backEvent -> backProgress.floatValue = backEvent.progress }
            backProgress.floatValue = 1F
            onBack()
        } catch (e: CancellationException) {
            backProgress.floatValue = 0F
        }
    }

    return backProgress
}