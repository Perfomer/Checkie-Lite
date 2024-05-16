package com.perfomer.checkielite.common.ui.cui.widget.toolbar

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiIconButton
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette

@Composable
fun CuiToolbarNavigationIcon(
    painter: Painter = painterResource(id = R.drawable.ic_cross),
    color: Color = LocalCuiPalette.current.IconPrimary,
    onBackPress: () -> Unit,
) {
    CuiIconButton(
        modifier = Modifier.padding(start = 8.dp),
        onClick = onBackPress,
        painter = painter,
        contentDescription = null,
        tint = color
    )
}