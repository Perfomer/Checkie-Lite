package group.bakemate.feature.splash.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface SplashScreenProvider {

    operator fun invoke(): CheckieScreen
}