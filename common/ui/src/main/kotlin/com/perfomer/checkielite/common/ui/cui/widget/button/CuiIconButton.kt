package com.perfomer.checkielite.common.ui.cui.widget.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@Composable
fun CuiIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    onClick: () -> Unit,
    tint: Color = LocalCuiPalette.current.IconPrimary,
    contentDescription: String? = null,
) {
	IconButton(
		modifier = modifier,
		onClick = onClick,
	) {
		Icon(
			painter = painter,
			tint = tint,
			contentDescription = contentDescription,
		)
	}
}

@Composable
@WidgetPreview
private fun CuiIconButtonPreview() {
	CheckieLiteTheme {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier.padding(24.dp)
		) {
			CuiIconButton(
				painter = painterResource(id = R.drawable.ic_cross),
				onClick = {},
			)
		}
	}
}