package group.bakemate.feature.splash.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import group.bakemate.common.ui.CommonDrawable
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.ScreenPreview

@Composable
internal fun SplashScreen() {
    val animationVisibleState = remember { MutableTransitionState(false) }
        .apply { targetState = true }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        AnimatedVisibility(
            visibleState = animationVisibleState,
            enter = fadeIn(tween(durationMillis = 2000)),
            exit = fadeOut(tween(durationMillis = 2000)),
        ) {
            Image(
                painter = painterResource(id = CommonDrawable.ic_logo),
                contentDescription = null,
            )
        }
    }
}

@ScreenPreview
@Composable
internal fun SplashScreenPreviewFilled() = PreviewTheme {
    SplashScreen()
}