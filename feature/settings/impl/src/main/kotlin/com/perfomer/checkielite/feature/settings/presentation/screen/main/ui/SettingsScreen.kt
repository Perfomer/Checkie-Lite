package com.perfomer.checkielite.feature.settings.presentation.screen.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import com.perfomer.checkielite.common.ui.cui.widget.info.CuiInfoIcon
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.app.AppLogo
import com.perfomer.checkielite.common.ui.util.app.appNameSpannable
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiState
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.widget.ConfirmBackupImportDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    state: SettingsUiState,

    shouldShowBackupImportConfirmDialog: Boolean = false,
    onBackupImportConfirmDialogDismiss: () -> Unit = {},
    onBackupImportConfirmDialogConfirm: () -> Unit = {},

    onNavigationIconClick: () -> Unit = {},
    onBackupExportClick: () -> Unit = {},
    onBackupImportClick: () -> Unit = {},
    onCheckUpdatesClick: () -> Unit = {},
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

            CuiSpacer(20.dp)

            AppGroup(
                isCheckUpdatesInProgress = state.isCheckUpdatesInProgress,
                onCheckUpdatesClick = onCheckUpdatesClick,
            )
        }

        if (shouldShowBackupImportConfirmDialog) {
            ConfirmBackupImportDialog(
                onDismiss = onBackupImportConfirmDialogDismiss,
                onConfirm = onBackupImportConfirmDialogConfirm,
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

        CuiSpacer(16.dp)

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
            title = stringResource(R.string.settings_group_data_title),
            infoText = stringResource(R.string.settings_group_data_description),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        CuiSpacer(8.dp)

        SettingsItem(
            title = stringResource(R.string.settings_group_data_item_export),
            subtitle = stringResource(R.string.settings_group_data_item_export_desc),
            icon = painterResource(R.drawable.ic_backup_export),
            onClick = onBackupExportClick,
        )

        SettingsItem(
            title = stringResource(R.string.settings_group_data_item_import),
            subtitle = stringResource(R.string.settings_group_data_item_import_desc),
            icon = painterResource(R.drawable.ic_backup_import),
            onClick = onBackupImportClick,
        )
    }
}

@Composable
private fun AppGroup(
    isCheckUpdatesInProgress: Boolean,
    onCheckUpdatesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        GroupTitle(
            title = stringResource(R.string.settings_group_app_title),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        CuiSpacer(8.dp)

        SettingsItem(
            title = stringResource(R.string.settings_group_app_item_check_updates),
            icon = painterResource(R.drawable.ic_update),
            onClick = onCheckUpdatesClick,
            endIcon = {
                AnimatedVisibility(
                    visible = isCheckUpdatesInProgress,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
        )
    }
}

@Composable
private fun GroupTitle(
    title: String,
    modifier: Modifier = Modifier,
    infoText: String? = null,
    description: String? = null,
) {
    Column(
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            if (infoText != null) {
                CuiSpacer(12.dp)
                CuiInfoIcon(text = infoText)
            }
        }

        if (description != null) {
            CuiSpacer(4.dp)

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
    title: String,
    subtitle: String? = null,
    icon: Painter,
    onClick: () -> Unit,
    endIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 9.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = LocalCuiPalette.current.IconAccent,
            modifier = Modifier.size(20.dp)
        )

        CuiSpacer(16.dp)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = LocalCuiPalette.current.TextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-2).dp)
                )
            }
        }

        endIcon?.invoke()
    }
}

@ScreenPreview
@Composable
private fun SettingsScreenPreview() = CheckieLiteTheme {
    SettingsScreen(state = mockUiState)
}

internal val mockUiState = SettingsUiState(
    appVersion = "1.0.0",
    isCheckUpdatesInProgress = false,
)