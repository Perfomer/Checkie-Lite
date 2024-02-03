package group.bakemate.feature.main.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface MainScreenProvider {

    operator fun invoke(): CheckieScreen
}