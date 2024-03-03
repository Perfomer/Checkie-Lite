package com.perfomer.checkielite.common.ui.cui.widget.toolbar

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
import com.perfomer.checkielite.common.ui.R
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiTitleText
import com.perfomer.checkielite.common.ui.theme.CuiColorToken
import com.perfomer.checkielite.common.ui.theme.PreviewTheme
import com.perfomer.checkielite.common.ui.theme.WidgetPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuiLargeToolbar(
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
                    containerColor = CuiColorToken.White,
                    titleContentColor = CuiColorToken.Black,
                ),
                title = { title() },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconCLick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
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
        CuiLargeToolbar(
            title = { CuiTitleText(text = "Регистрация")},
            navigationColor = CuiColorToken.Orange,
            onNavigationIconCLick = {},
            actions = {}
        ) {}
    }
}