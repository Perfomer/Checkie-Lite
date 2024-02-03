package group.bakemate.feature.settings.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface SettingsScreenProvider {

    operator fun invoke(): CheckieScreen
}