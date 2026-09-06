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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perfomer.checkielite.common.ui.CommonDrawable
import com.perfomer.checkielite.common.ui.cui.modifier.toolbarDivider
import com.perfomer.checkielite.common.ui.cui.widget.info.CuiInfoIcon
import com.perfomer.checkielite.common.ui.cui.widget.spacer.CuiSpacer
import com.perfomer.checkielite.common.ui.cui.widget.text.CuiFadedText
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiGlassScaffold
import com.perfomer.checkielite.common.ui.cui.widget.toolbar.CuiToolbarNavigationIcon
import com.perfomer.checkielite.common.ui.theme.CheckieLiteTheme
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.common.ui.theme.ScreenPreview
import com.perfomer.checkielite.common.ui.util.app.AppLogo
import com.perfomer.checkielite.common.ui.util.app.appNameSpannable
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.common.ui.util.resource.text.text
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
    onChangelogClick: () -> Unit = {},
    onLanguageSettingsClick: () -> Unit = {},
    onThemeSettingsClick: () -> Unit = {},
    onLiquidGlassChanged: (Boolean) -> Unit = {},
    onLibrariesClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val shouldShowDivider by remember { derivedStateOf { scrollState.canScrollBackward } }

    CuiGlassScaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontSize = 18.sp, fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    CuiToolbarNavigationIcon(
                        painter = painterResource(CommonDrawable.ic_cross),
                        color = LocalCuiPalette.current.IconPrimary,
                        onBackPress = onNavigationIconClick,
                    )
                },
                modifier = Modifier.toolbarDivider(
                    show = shouldShowDivider,
                    strokeColor = LocalCuiPalette.current.OutlineSecondary,
                )
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(contentPadding)
        ) {
            SettingsHeader(version = state.appVersion)

            BackupGroup(
                onBackupExportClick = onBackupExportClick,
                onBackupImportClick = onBackupImportClick,
            )

            CuiSpacer(20.dp)

            AppGroup(
                state = state,
                isCheckUpdatesInProgress = state.isCheckUpdatesInProgress,
                onCheckUpdatesClick = onCheckUpdatesClick,
                onChangelogClick = onChangelogClick,
                onLanguageSettingsClick = onLanguageSettingsClick,
                onLibrariesClick = onLibrariesClick,
                onThemeSettingsClick = onThemeSettingsClick,
                onLiquidGlassChanged = onLiquidGlassChanged,
            )
        }

        ConfirmBackupImportDialog(
            isVisible = shouldShowBackupImportConfirmDialog,
            onDismiss = onBackupImportConfirmDialogDismiss,
            onConfirm = onBackupImportConfirmDialogConfirm,
        )
    }
}

@Composable
private fun SettingsHeader(version: Text) {
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
            text = text(version),
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
    state: SettingsUiState,
    isCheckUpdatesInProgress: Boolean,
    onCheckUpdatesClick: () -> Unit,
    onChangelogClick: () -> Unit,
    onLanguageSettingsClick: () -> Unit,
    onThemeSettingsClick: () -> Unit,
    onLiquidGlassChanged: (Boolean) -> Unit,
    onLibrariesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        GroupTitle(
            title = stringResource(R.string.settings_group_app_title),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        CuiSpacer(8.dp)

        SettingsItem(
            title = stringResource(R.string.settings_group_app_item_language_settings),
            subtitle = text(state.currentLanguage),
            icon = painterResource(R.drawable.ic_language),
            onClick = onLanguageSettingsClick,
            endIcon = {
                Icon(
                    painter = painterResource(CommonDrawable.ic_chevron_right),
                    tint = LocalCuiPalette.current.IconAccent,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        SettingsItem(
            title = stringResource(R.string.settings_group_app_item_theme),
            subtitle = text(state.themeMode),
            icon = painterResource(state.themeIcon),
            onClick = onThemeSettingsClick,
        )

        LiquidGlassToggle(
            checked = state.isLiquidGlassEnabled,
            enabled = !state.isLiquidGlassChangeInProgress,
            onCheckedChange = onLiquidGlassChanged,
        )

        SettingsItem(
            title = stringResource(R.string.settings_group_app_item_changelog),
            subtitle = stringResource(R.string.settings_group_app_item_changelog_desc),
            icon = painterResource(CommonDrawable.ic_info),
            onClick = onChangelogClick,
            endIcon = {
                Icon(
                    painter = painterResource(CommonDrawable.ic_chevron_right),
                    tint = LocalCuiPalette.current.IconAccent,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        SettingsItem(
            title = stringResource(R.string.settings_group_app_item_check_updates),
            subtitle = stringResource(R.string.settings_group_app_item_check_updates_desc),
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
private fun LiquidGlassToggle(
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val palette = LocalCuiPalette.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .defaultMinSize(minHeight = 56.dp)
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            )
            .padding(horizontal = 20.dp, vertical = 9.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_liquid_glass),
            contentDescription = null,
            tint = palette.IconAccent,
            modifier = Modifier.size(20.dp)
        )

        CuiSpacer(16.dp)

        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(R.string.settings_group_app_item_liquid_glass),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = stringResource(R.string.settings_group_app_item_liquid_glass_desc),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = palette.TextSecondary,
            )
        }

        CuiSpacer(12.dp)

        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = palette.IconSecondary,
                uncheckedTrackColor = palette.BackgroundSecondary,
                uncheckedBorderColor = palette.OutlinePrimary,
                disabledUncheckedThumbColor = palette.IconQuaternary,
                disabledUncheckedTrackColor = palette.BackgroundSecondary,
                disabledUncheckedBorderColor = palette.OutlineSecondary,
            ),
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
            CuiFadedText(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            if (subtitle != null) {
                CuiFadedText(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = LocalCuiPalette.current.TextSecondary,
                    maxLines = 1,
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
    appVersion = Text.raw("1.0.0"),
    isCheckUpdatesInProgress = false,
    currentLanguage = Text.raw("English"),
    themeIcon = R.drawable.ic_theme_system,
    themeMode = Text.raw("System"),
    isLiquidGlassEnabled = true,
    isLiquidGlassChangeInProgress = false,
)
