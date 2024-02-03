package group.bakemate.common.ui.bui.button

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
import group.bakemate.common.ui.R
import group.bakemate.common.ui.theme.Black
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.WidgetPreview

@Composable
fun BuiIconButton(
	modifier: Modifier = Modifier,
	painter: Painter,
	onClick: () -> Unit,
	tint: Color = Black,
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
	PreviewTheme {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier.padding(24.dp)
		) {
			BuiIconButton(
				painter = painterResource(id = R.drawable.ic_exit_cross),
				onClick = {},
			)
		}
	}
}