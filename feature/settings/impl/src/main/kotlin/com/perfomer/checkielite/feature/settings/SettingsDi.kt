package com.perfomer.checkielite.feature.settings

import android.content.Context
import com.perfomer.checkielite.common.android.apprestart.AppRestarter
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.data.repository.BackupRepository
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.main.navigation.MainScreenProvider
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupParams
import com.perfomer.checkielite.feature.settings.presentation.navigation.BackupScreenProvider
import com.perfomer.checkielite.feature.settings.presentation.navigation.SettingsScreenProvider
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
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ExportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.ImportBackupActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.actor.SettingsNavigationActor
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.SettingsContentScreen
import com.perfomer.checkielite.feature.settings.presentation.screen.main.ui.state.SettingsUiStateMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsModules
    get() = listOf(presentationModule)

private val presentationModule = module {
    factoryOf(::createSettingsStore)
    factory { SettingsScreenProvider(::SettingsContentScreen) }

    factoryOf(::createBackupStore)
    factory { BackupScreenProvider(::BackupContentScreen) }
}

internal fun createSettingsStore(
    context: Context,
    router: Router,
    externalRouter: ExternalRouter,
    localDataSource: CheckieLocalDataSource,
    backupRepository: BackupRepository,
): SettingsStore {
    return SettingsStore(
        reducer = SettingsReducer(),
        uiStateMapper = SettingsUiStateMapper(context),
        actors = setOf(
            SettingsNavigationActor(router, externalRouter),
            ExportBackupActor(backupRepository),
            ImportBackupActor(backupRepository),
            CheckSyncingActor(localDataSource),
            CheckHasReviewsActor(localDataSource),
        ),
    )
}

internal fun createBackupStore(
    params: BackupParams,
    context: Context,
    router: Router,
    appRestarter: AppRestarter,
    backupRepository: BackupRepository,
    mainScreenProvider: MainScreenProvider,
): BackupStore {
    return BackupStore(
        params = params,
        reducer = BackupReducer(),
        uiStateMapper = BackupUiStateMapper(context),
        actors = setOf(
            BackupNavigationActor(router, appRestarter, mainScreenProvider),
            ObserveBackupProgressActor(backupRepository),
            AwaitActor(),
            CancelBackupActor(backupRepository),
        ),
    )
}