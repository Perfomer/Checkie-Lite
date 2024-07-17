package com.perfomer.checkielite.feature.settings

import android.content.Context
import com.perfomer.checkielite.common.android.AppRestarter
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.core.navigation.api.ExternalRouter
import com.perfomer.checkielite.core.navigation.api.Router
import com.perfomer.checkielite.feature.settings.presentation.navigation.SettingsScreenProvider
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsReducer
import com.perfomer.checkielite.feature.settings.presentation.screen.main.tea.SettingsStore
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
}

internal fun createSettingsStore(
    context: Context,
    router: Router,
    externalRouter: ExternalRouter,
    appRestarter: AppRestarter,
    localDataSource: CheckieLocalDataSource,
): SettingsStore {
    return SettingsStore(
        reducer = SettingsReducer(),
        uiStateMapper = SettingsUiStateMapper(context),
        actors = setOf(
            SettingsNavigationActor(router, externalRouter, appRestarter),
            ExportBackupActor(localDataSource),
            ImportBackupActor(localDataSource),
            CheckSyncingActor(localDataSource),
        ),
    )
}