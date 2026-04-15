package com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.domain.entity.theme.ThemeMode

internal sealed interface SettingsCommand {

    data object LoadCurrentLocale : SettingsCommand

    data object LoadTheme : SettingsCommand

    data object CheckSyncing : SettingsCommand

    data object CheckHasReviews : SettingsCommand

    data object ExportBackup : SettingsCommand

    class ImportBackup(val path: String) : SettingsCommand

    data object CheckUpdates : SettingsCommand

    data object LaunchAppUpdate : SettingsCommand
}

internal sealed interface SettingsNavigationCommand : SettingsCommand {

    data object Exit : SettingsNavigationCommand

    data object SelectBackupFile : SettingsNavigationCommand

    data object OpenLanguageSettings : SettingsNavigationCommand

    data object OpenChangelog : SettingsNavigationCommand

    data object OpenLibraries : SettingsNavigationCommand

    class OpenThemeSettings(val currentTheme: ThemeMode) : SettingsNavigationCommand
}