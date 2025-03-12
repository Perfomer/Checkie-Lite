package com.perfomer.checkielite.common.ui.cui.widget.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun ToastHost(
    controller: ToastController = LocalToastController.current,
    toast: @Composable (ToastData) -> Unit = { data ->
        CuiToast(
            data = data,
            onClick = controller::dismiss,
            onSwipeOut = controller::dismiss,
        )
    },
) {
    val currentToastData = controller.currentToastData

    LaunchedEffect(controller.show) {
        if (currentToastData != null && controller.show) {
            delay(currentToastData.durationMs)
            controller.dismiss()
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = controller.show && currentToastData != null,
            enter = slideInVertically(),
            exit = slideOutVertically(targetOffsetY = { -it }),
        ) {
            toast(currentToastData!!)
        }
    }
}