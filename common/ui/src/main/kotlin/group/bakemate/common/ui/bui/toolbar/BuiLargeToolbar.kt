package group.bakemate.common.ui.bui.toolbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import group.bakemate.common.ui.R
import group.bakemate.common.ui.bui.text.BuiTitleText
import group.bakemate.common.ui.theme.Black
import group.bakemate.common.ui.theme.Orange
import group.bakemate.common.ui.theme.PreviewTheme
import group.bakemate.common.ui.theme.White
import group.bakemate.common.ui.theme.WidgetPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuiLargeToolbar(
    title: @Composable () -> Unit,
    navigationColor: Color,
    onNavigationIconCLick: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = White,
                    titleContentColor = Black,
                ),
                title = { title() },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconCLick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_exit_cross),
                            contentDescription = null,
                            tint = navigationColor,
                        )
                    }
                },
                actions = actions,
                scrollBehavior = scrollBehavior
            )
        },
        content = { paddingValues ->
            content(paddingValues)
        }
    )
}

@Composable
@WidgetPreview
private fun CuiLargeToolbarPreview() {
    PreviewTheme {
        BuiLargeToolbar(
            title = { BuiTitleText(text = "Регистрация")},
            navigationColor = Orange,
            onNavigationIconCLick = {},
            actions = {}
        ) {}
    }
}