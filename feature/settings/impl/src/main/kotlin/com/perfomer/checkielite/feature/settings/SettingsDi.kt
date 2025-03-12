package com.perfomer.checkielite.feature.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.perfomer.checkielite.common.android.apprestart.AppRestarter
import com.perfomer.checkielite.common.update.api.AppUpdateManager
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.data.repository.ReviewRepository
import com.perfomer.checkielite.core.navigation.ExternalRouter
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.associate
import com.perfomer.checkielite.core.navigation.navigation
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupDestination
import com.perfomer.checkielite.feature.settings.presentation.navigation.SettingsDestination
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.BackupStore
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.AwaitActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.BackupNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.CancelBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.actor.ObserveBackupProgressActor
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.BackupContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.ui.state.BackupUiStateMapper
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckHasReviewsActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckSyncingActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.CheckUpdatesActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ExportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ImportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.SettingsNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.SettingsContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiStateMapper
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
    }

    factoryOf(::createSettingsStore)
    factoryOf(::SettingsContentScreen)

    factoryOf(::createBackupStore)
    factoryOf(::BackupContentScreen)
}

internal fun createSettingsStore(
    componentContext: ComponentContext,
    destination: SettingsDestination,
    context: Context,
    router: Router,
    externalRouter: ExternalRouter,
    localDataSource: CheckieLocalDataSource,
    reviewRepository: ReviewRepository,
    backupRepository: BackupRepository,
    appUpdateManager: AppUpdateManager,
): SettingsStore {
    return SettingsStore(
        componentContext = componentContext,
        destination = destination,
        reducer = SettingsReducer(),
        uiStateMapper = SettingsUiStateMapper(context),
        actors = setOf(
            SettingsNavigationActor(router, externalRouter),
            ExportBackupActor(backupRepository),
            ImportBackupActor(backupRepository),
            CheckSyncingActor(localDataSource),
            CheckHasReviewsActor(reviewRepository),
            CheckUpdatesActor(appUpdateManager),
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