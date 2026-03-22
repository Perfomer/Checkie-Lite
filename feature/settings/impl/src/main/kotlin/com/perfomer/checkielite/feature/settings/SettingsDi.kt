package com.perfomer.checkielite.feature.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.android.apprestart.AppRestarter
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.core.data.repository.AppRepository
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.navigation.ExternalRouter
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.core.theme.manager.ThemeManager
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.LibrariesDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.SettingsDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.ThemeDestination
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.AwaitActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.BackupNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.CancelBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.ObserveBackupProgressActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.BackupContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.libraries.ui.LibrariesContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckHasReviewsActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckSyncingActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckUpdatesActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ExportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ImportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.LoadThemeActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.SettingsNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.SettingsContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.ThemeReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.ThemeStore
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.actor.SetThemeActor
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.tea.actor.ThemeNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.ThemeContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.theme.ui.state.ThemeUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.LaunchAppUpdateActor as BackupLaunchAppUpdateActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.LaunchAppUpdateActor as SettingsLaunchAppUpdateActor

val settingsModules
    get() = listOf(presentationModule)

private val presentationModule = module {
    navigation {
        associate<SettingsDestination, SettingsContentScreen>()
        associate<BackupDestination, BackupContentScreen>()
        associate<LibrariesDestination, LibrariesContentScreen>()
        associate<ThemeDestination, ThemeContentScreen>()
    }

    factoryOf(::createSettingsStore)
    factoryOf(::SettingsContentScreen)

    factoryOf(::createBackupStore)
    factoryOf(::BackupContentScreen)

    factoryOf(::LibrariesContentScreen)

    factoryOf(::createThemeStore)
    factoryOf(::ThemeContentScreen)
}

internal fun createSettingsStore(
    componentContext: ComponentContext,
    router: Router,
    context: Context,
    externalRouter: ExternalRouter,
    themeManager: ThemeManager,
    appRepository: AppRepository,
    backupRepository: BackupRepository,
    reviewRepository: ReviewRepository,
    appUpdateManager: AppUpdateManager,
): SettingsStore {
    return SettingsStore(
        componentContext = componentContext,
        reducer = SettingsReducer(),
        uiStateMapper = SettingsUiStateMapper(context),
        actors = setOf(
            SettingsNavigationActor(router, externalRouter),
            ExportBackupActor(backupRepository),
            ImportBackupActor(backupRepository),
            CheckSyncingActor(appRepository),
            CheckHasReviewsActor(reviewRepository),
            CheckUpdatesActor(appUpdateManager),
            LoadThemeActor(themeManager),
            SettingsLaunchAppUpdateActor(appUpdateManager),
        ),
    )
}

internal fun createBackupStore(
    componentContext: ComponentContext,
    destination: BackupDestination,
    context: Context,
    router: Router,
    appRestarter: AppRestarter,
    backupRepository: BackupRepository,
    appUpdateManager: AppUpdateManager,
): BackupStore {
    return BackupStore(
        componentContext = componentContext,
        destination = destination,
        reducer = BackupReducer(),
        uiStateMapper = BackupUiStateMapper(context),
        actors = setOf(
            BackupNavigationActor(router, appRestarter),
            ObserveBackupProgressActor(backupRepository),
            AwaitActor(),
            CancelBackupActor(backupRepository),
            BackupLaunchAppUpdateActor(appUpdateManager),
        ),
    )
}

internal fun createThemeStore(
    componentContext: ComponentContext,
    destination: ThemeDestination,
    context: Context,
    router: Router,
    themeManager: ThemeManager,
): ThemeStore {
    return ThemeStore(
        componentContext = componentContext,
        destination = destination,
        reducer = ThemeReducer(),
        uiStateMapper = ThemeUiStateMapper(context),
        actors = setOf(
            SetThemeActor(themeManager),
            ThemeNavigationActor(router),
        ),
    )
}