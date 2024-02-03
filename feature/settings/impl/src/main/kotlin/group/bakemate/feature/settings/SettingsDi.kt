package group.bakemate.feature.settings

import group.bakemate.feature.settings.navigation.SettingsScreenProvider
import group.bakemate.feature.settings.presentation.SettingsContentScreen
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsScreenProvider(::SettingsContentScreen) }
}