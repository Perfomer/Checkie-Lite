package com.perfomer.checkielite.common.ui.util.navigation

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun registerPredictiveBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
): FloatState {
    val backProgress = remember { mutableFloatStateOf(0F) }

    PredictiveBackHandler(
        enabled = enabled,
        onBack = onBack,
        onProgress = { progress -> backProgress.floatValue = progress },
    )

    return backProgress
}

@Composable
fun PredictiveBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
    onProgress: (progress: Float) -> Unit,
) {
    PredictiveBackHandler(enabled = enabled) { progress ->
        try {
            progress.map { it.progress }
                .collect(onProgress)

            onProgress(1F)
            onBack()
        } catch (e: CancellationException) {
            onProgress(0F)
        }
    }
}