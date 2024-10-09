package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.app.AppLogo
import com.perfomer.checkielite.common.ui.util.app.appNameSpannable
import com.perfomer.checkielite.feature.settings.R
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
                title = { Text(stringResource(R.string.settings_title), fontSize = 18.sp, fontWeight = FontWeight.Medium) },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            SettingsHeader(version = state.appVersion)

            BackupGroup(
                onBackupExportClick = onBackupExportClick,
                onBackupImportClick = onBackupImportClick,
            )
        }
    }
}

@Composable
private fun SettingsHeader(version: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        AppLogo(
            modifier = Modifier.size(width = 66.dp, height = 72.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = appNameSpannable(),
            fontSize = 24.sp,
            letterSpacing = (0).sp,
        )

        Text(
            text = version,
            color = LocalCuiPalette.current.TextSecondary,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun BackupGroup(
    onBackupExportClick: () -> Unit,
    onBackupImportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        GroupTitle(
            title = stringResource(R.string.settings_group_backup_title),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(8.dp))

        SettingsItem(
            text = stringResource(R.string.settings_group_backup_item_export),
            icon = painterResource(R.drawable.ic_backup_export),
            onClick = onBackupExportClick,
        )

        SettingsItem(
            text = stringResource(R.string.settings_group_backup_item_import),
            icon = painterResource(R.drawable.ic_backup_import),
            onClick = onBackupImportClick,
        )
    }
}

@Composable
private fun GroupTitle(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        if (description != null) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = LocalCuiPalette.current.TextSecondary,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
            )
        }
    }
}

@Composable
private fun SettingsItem(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = LocalCuiPalette.current.IconAccent,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenPreview() = CheckieLiteTheme {
    SettingsScreen(state = mockUiState)
}

internal val mockUiState = SettingsUiState(
    appVersion = "1.0.0",
)