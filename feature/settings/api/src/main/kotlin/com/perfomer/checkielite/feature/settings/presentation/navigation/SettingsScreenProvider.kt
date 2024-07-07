package com.perfomer.checkielite.feature.settings.presentation.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface SettingsScreenProvider {

    operator fun invoke(): CheckieScreen
}