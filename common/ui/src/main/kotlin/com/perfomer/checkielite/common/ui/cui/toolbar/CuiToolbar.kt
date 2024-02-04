package com.perfomer.checkielite.common.ui.cui.toolbar

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
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.cui.button.CuiIconButton
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuiToolbar(
    modifier: Modifier = Modifier,
    navigationColor: Color = CuiColorToken.Orange,
    title: String = "",
    onBackPress: () -> Unit,
) {
    TopAppBar(
        title = { ToolbarTitle(title) },
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
private fun ToolbarTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = CuiColorToken.Black,
    )
}

@Composable
fun CuiToolbarNavigationIcon(
    painter: Painter = painterResource(id = R.drawable.ic_exit_cross),
    color: Color,
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

@Composable
@WidgetPreview
private fun CuiToolbarPreview() {
    PreviewTheme {
        CuiToolbar(
            title = "Title",
            onBackPress = {}
        )
    }
}