package com.perfomer.checkielite.common.ui.cui.widget.progress

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

private const val ANIMATION_DURATION_MS = 500

@Composable
fun CuiProgressBar(
	progress: Float,
	modifier: Modifier = Modifier,
	backgroundColor: Color = LocalCuiPalette.current.BackgroundSecondary,
	progressColor: Color = LocalCuiPalette.current.BackgroundAccentPrimary,
	progressFullColor: Color = LocalCuiPalette.current.BackgroundPositive,
) {
	val targetProgressColor =
		if (progress >= 1F) progressFullColor
		else progressColor

	val animatedProgress = animateFloatAsState(
		targetValue = progress,
		animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
		label = "animatedProgress",
	)

	val animatedProgressColor = animateColorAsState(
		targetValue = targetProgressColor,
		animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
		label = "animatedProgressColor",
	)

	LinearProgressIndicator(
		progress = { animatedProgress.value },
		trackColor = backgroundColor,
		color = animatedProgressColor.value,
		strokeCap = StrokeCap.Round,
		modifier = modifier.height(8.dp)
			.clip(CircleShape),
	)
}

@WidgetPreview
@Composable
private fun CuiProgressBarPreview() {
	PreviewTheme {
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.padding(24.dp)
		) {
			CuiProgressBar(progress = 0.00F)
			CuiProgressBar(progress = 0.25F)
			CuiProgressBar(progress = 0.50F)
			CuiProgressBar(progress = 0.75F)
			CuiProgressBar(progress = 1.00F)
		}
	}
}