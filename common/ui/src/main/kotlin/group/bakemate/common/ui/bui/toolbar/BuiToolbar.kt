package group.bakemate.common.ui.bui.toolbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import group.bakemate.common.ui.R
import group.bakemate.common.ui.bui.button.BuiIconButton
import group.bakemate.common.ui.theme.Black
import group.bakemate.common.ui.theme.Orange
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.WidgetPreview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuiToolbar(
    modifier: Modifier = Modifier,
    navigationColor: Color = Orange,
    title: String = "",
    onBackPress: () -> Unit,
) {
    TopAppBar(
        title = { BuiToolbarTitle(title) },
        navigationIcon = {
            CuiToolbarNavigationIcon(
                onBackPress = onBackPress,
                color = navigationColor
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
private fun BuiToolbarTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Black,
    )
}

@Composable
fun CuiToolbarNavigationIcon(
    painter: Painter = painterResource(id = R.drawable.ic_exit_cross),
    color: Color,
    onBackPress: () -> Unit,
) {
    BuiIconButton(
        modifier = Modifier.padding(start = 8.dp),
        onClick = onBackPress,
        painter = painter,
        contentDescription = null,
        tint = color
    )
}

@Composable
@WidgetPreview
private fun CuiToolbarPreview() {
    PreviewTheme {
        BuiToolbar(
            title = "Title",
            onBackPress = {}
        )
    }
}