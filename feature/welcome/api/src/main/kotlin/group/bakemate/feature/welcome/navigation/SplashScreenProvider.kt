package group.bakemate.feature.welcome.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface WelcomeScreenProvider {

    operator fun invoke(): CheckieScreen
}