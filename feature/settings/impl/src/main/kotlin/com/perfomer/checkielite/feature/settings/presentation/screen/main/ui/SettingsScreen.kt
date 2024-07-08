package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.button.CuiOutlineButton
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
    onBackupExportClick: () -> Unit = {},
    onBackupImportClick: () -> Unit = {},
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
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            CuiOutlineButton(
                text = "Export",
                onClick = onBackupExportClick,
            )

            CuiOutlineButton(
                text = "Import",
                onClick = onBackupImportClick,
            )

            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenPreview() = CheckieLiteTheme {
    SettingsScreen(state = mockUiState)
}

internal val mockUiState = SettingsUiState(
    isLoading = false,
)