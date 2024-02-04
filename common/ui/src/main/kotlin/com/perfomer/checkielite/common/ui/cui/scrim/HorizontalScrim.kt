package com.perfomer.checkielite.common.ui.cui.scrim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun HorizontalScrim(
	modifier: Modifier = Modifier,
	color: Color = Color.White,
) {
	val brush = remember {
		Brush.horizontalGradient(colors = listOf(color.copy(alpha = 0F), color))
	}

	Box(modifier = modifier.background(brush))
}