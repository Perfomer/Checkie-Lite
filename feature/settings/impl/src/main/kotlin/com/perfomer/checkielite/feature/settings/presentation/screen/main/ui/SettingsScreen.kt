package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    onNavigationIconClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    CuiToolbarNavigationIcon(
                        painter = painterResource(CommonDrawable.ic_cross),
                        color = LocalCuiPalette.current.IconPrimary,
                        onBackPress = onNavigationIconClick,
                    )
                }
            )
        },
    ) { contentPadding ->
        Text("Temporary")
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenPreview() = CheckieLiteTheme {
    SettingsScreen(state = mockUiState)
}

internal val mockUiState = SettingsUiState