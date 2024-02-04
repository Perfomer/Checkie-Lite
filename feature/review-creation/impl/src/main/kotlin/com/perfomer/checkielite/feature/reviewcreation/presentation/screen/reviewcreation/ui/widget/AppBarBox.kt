package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun AppBarBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 0.dp,
    shape: Shape = RectangleShape,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            content = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .height(56.dp),
        )
    }
}