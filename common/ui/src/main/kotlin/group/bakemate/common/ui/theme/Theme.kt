package group.bakemate.common.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme

@Composable
fun BakemateTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        content = { CompositionLocalContent(content) },
    )
}

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        content = { CompositionLocalContent(content) },
    )
}

@Composable
private fun CompositionLocalContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(
//        LocalShimmerTheme provides BakemateShimmerTheme,
//        LocalRippleTheme provides BakemateRippleTheme,
        content = content,
    )
}


@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.White.copy(alpha = 0.01F),
            darkIcons = true,
        )

        systemUiController.setStatusBarColor(
            color = Color.White.copy(alpha = 0.01F),
            darkIcons = true,
        )
    }
}

private val LightAndroidColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = White,
    secondary = GreyLight,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    error = Red,
    onError = White,
)

private val BakemateShimmerTheme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 600,
            easing = LinearEasing,
            delayMillis = 800,
        ),
        repeatMode = RepeatMode.Restart,
    ),
    shaderColors = listOf(
        Color.Unspecified.copy(alpha = 0.50F),
        Color.Unspecified.copy(alpha = 1.00F),
        Color.Unspecified.copy(alpha = 0.50F),
    ),
    shaderColorStops = listOf(
        0.25F,
        0.50F,
        1.00F,
    ),
    shimmerWidth = 300.dp,
)

private object BakemateRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = Pink

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = Pink,
        lightTheme = true
    )
}