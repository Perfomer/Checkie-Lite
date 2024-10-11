package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun CuiFloatingActionButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = LocalCuiPalette.current.SmallElevation,
        pressedElevation = 0.dp,
    ),
) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = onClick,
        interactionSource = interactionSource,
        elevation = elevation,
        modifier = modifier
    ) {
        Icon(painter = painter, contentDescription = contentDescription)
    }
}