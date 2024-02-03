package group.bakemate.feature.registration.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface RegistrationScreenProvider {

    operator fun invoke(): CheckieScreen
}